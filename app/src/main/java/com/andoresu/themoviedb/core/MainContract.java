package com.andoresu.themoviedb.core;

import com.andoresu.themoviedb.core.authorization.data.Session;
import com.andoresu.themoviedb.utils.BaseActionListener;
import com.andoresu.themoviedb.utils.BaseView;

public interface MainContract {
    interface View extends BaseView {
        void loginRequired();
    }

    interface ActionsListener extends BaseActionListener {

        void logout(Session session);

    }
}
