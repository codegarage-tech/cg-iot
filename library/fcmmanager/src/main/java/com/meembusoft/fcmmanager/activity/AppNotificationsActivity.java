package com.meembusoft.fcmmanager.activity;

import android.content.BroadcastReceiver;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.jaeger.library.StatusBarUtil;
import com.meembusoft.fcmmanager.R;
import com.meembusoft.fcmmanager.adapter.MessageAdapter;
import com.meembusoft.fcmmanager.payload.Payload;
import com.meembusoft.fcmmanager.util.FcmUtil;
import com.meembusoft.fcmmanager.util.Message;
import com.meembusoft.fcmmanager.util.Messages;
import com.meembusoft.fcmmanager.util.Notifications;
import com.meembusoft.fcmmanager.util.Presence;
import com.meembusoft.fcmmanager.util.SpacingItemDecoration;
import com.meembusoft.fcmmanager.views.AnimatedImageView;
import com.meembusoft.fcmmanager.views.AnimatedTextView;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class AppNotificationsActivity extends AppCompatActivity {

    //Toolbar
    private AnimatedImageView leftMenu;
    private ImageView rightMenu;
    private AnimatedTextView toolbarTitle;

    private MessageAdapter adapter;
    private View emptyView;
    private RecyclerView recyclerView;
    private Messages messages;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_notifications);
        initUI();
        initActions();
    }

    private void initUI() {
        //toolbar view
        toolbarTitle = (AnimatedTextView) findViewById(R.id.toolbar_title);
        leftMenu = (AnimatedImageView) findViewById(R.id.left_menu);
        rightMenu = (ImageView) findViewById(R.id.right_menu);

        setToolBarTitle(getString(R.string.title_activity_app_notifications));
        StatusBarUtil.setTransparent(AppNotificationsActivity.this);

        messages = Messages.instance(this);
        initRecyclerView();
        registerReceiver();
    }

    private void initActions() {
        leftMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void setToolBarTitle(String title) {
        toolbarTitle.setAnimatedText(title, 0L);

        //For marquee address
        FcmUtil.applyMarqueeOnTextView(toolbarTitle);
    }

    private void initRecyclerView() {
        emptyView = findViewById(R.id.empty_view);
        recyclerView = findViewById(R.id.recycler_view);
        Resources resources = getResources();
        int horizontal = resources.getDimensionPixelSize(R.dimen.unit_10);
        int vertical = resources.getDimensionPixelSize(R.dimen.unit_10);
        recyclerView.addItemDecoration(new SpacingItemDecoration(horizontal, vertical));
        recyclerView.setHasFixedSize(false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MessageAdapter(messages.get());
        adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                final int position = viewHolder.getAdapterPosition();
                Message<Payload> removed = adapter.removeItemAtPosition(position);
                messages.remove(removed);
                onAdapterCountMightHaveChanged();
                String message = getString(R.string.snackbar_item_deleted, 1);
                Snackbar snackbar = Snackbar.make(recyclerView, message, Snackbar.LENGTH_LONG);
                snackbar.setAction(R.string.snackbar_item_undo, v -> messages.add(removed)).show();
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        onAdapterCountMightHaveChanged();
    }

    private void onAdapterCountMightHaveChanged() {
        int count = adapter != null ? adapter.getItemCount() : 0;
        emptyView.setVisibility(count > 0 ? View.INVISIBLE : View.VISIBLE);
    }

    private final BroadcastReceiver presenceReceiver = Presence.create(new Presence.Handler() {
        @Override
        public void handle(boolean presence) {

        }
    });

    private void registerReceiver() {
        Presence.register(AppNotificationsActivity.this, presenceReceiver);
        messages.register(new Messages.Listener() {
            @Override
            public void onNewMessage(@NonNull Message<Payload> message) {
                int index = adapter.add(message);
                recyclerView.smoothScrollToPosition(index);
                onAdapterCountMightHaveChanged();
            }
        });
    }

    private void unregisterReceiver() {
        Presence.unregister(AppNotificationsActivity.this, presenceReceiver);
        messages.unregister(new Messages.Listener() {
            @Override
            public void onNewMessage(@NonNull Message<Payload> message) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Notifications.removeAll(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver();
    }
}