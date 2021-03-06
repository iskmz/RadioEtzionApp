package com.hackathon.radioetzionapp.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hackathon.radioetzionapp.Data.BroadcastDataClass;
import com.hackathon.radioetzionapp.Data.Defaults;
import com.hackathon.radioetzionapp.Data.FavoritesSharedPref;
import com.hackathon.radioetzionapp.R;
import com.hackathon.radioetzionapp.Utils.Utils;

import java.util.List;

public class BroadcastListAdapter extends BaseAdapter implements Animation.AnimationListener {



    private List<BroadcastDataClass> lst;
    private Context context;
    private FavoritesSharedPref favoritesSharedPref;

    private Animation entryRight, entryLeft;

    public BroadcastListAdapter(Context context,List<BroadcastDataClass> lst) {
        this.context=context;
        this.lst = lst;
        favoritesSharedPref = new FavoritesSharedPref(context);

        // animation initialize
        entryRight = AnimationUtils.loadAnimation(context, R.anim.entry_from_right);
        entryRight.setAnimationListener(this);
        entryLeft = AnimationUtils.loadAnimation(context, R.anim.entry_from_left);
        entryLeft.setAnimationListener(this);
    }


    @Override
    public int getCount() {
        return lst.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;

        view = getBroadcastItemView(position);

        return view;
    }

    private View getBroadcastItemView(final int position) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_broadcast, null);

        // link ids to pointers
        ImageView imgAlbum = v.findViewById(R.id.img_ItemBroadcast);
        final ImageView imgFav = v.findViewById(R.id.imgFav_ItemBroadcast);
        TextView txtTitle = v.findViewById(R.id.txtBroadcastTitle_ItemBroadcast);

        // connect data & adjust views
        imgAlbum.setColorFilter(getAlternateColor(position));
        txtTitle.setTextColor(getAlternateColor(position));
        txtTitle.setText(lst.get(position).getTitle());
        setFavoritesStatus(imgFav, position);

        // set listener for imgAlbum onClick
        imgAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showTrackInfoDialog(context, position);
            }
        });

        // set listener for imgFav onClick
        imgFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemTitle = Defaults.dataList.get(position).getTitle();
                // change value according to old
                if (favoritesSharedPref.isInFav(itemTitle))
                    favoritesSharedPref.removeFromFav(itemTitle);
                else favoritesSharedPref.addtoFav(itemTitle);
                // update image according to new value
                setFavoritesStatus(imgFav, position);
            }
        });

        // adjust main view (parent)
        v.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        v.setTextDirection(View.LAYOUT_DIRECTION_RTL);
        v.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);

        // set entry animation for text & image
        txtTitle.setAnimation(entryRight);
        imgAlbum.setAnimation(entryRight);
        // set entry animation for fav icon
        imgFav.setAnimation(entryLeft);

        return v;

    }

    private void setFavoritesStatus(ImageView imgFav, int position) {

        // toggle icon based on shared pref value (in or out of fav. list)
        String itemTitle = Defaults.dataList.get(position).getTitle();
        imgFav.setImageResource(favoritesSharedPref.isInFav(itemTitle)
                ? R.drawable.ic_favorite : R.drawable.ic_favorite_border);

        // default color
        imgFav.setColorFilter(getAlternateColor(position));
    }

    private int getAlternateColor(int pos) {

        int returnValue = Color.WHITE;

        switch (pos % 2) {

            case 0:
                returnValue = ContextCompat.getColor(context, R.color.album_alternate_1);
                break;
            case 1:
                returnValue = ContextCompat.getColor(context, R.color.album_alternate_2);
                break;
        }

        return returnValue;
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
