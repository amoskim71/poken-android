package id.unware.poken.ui.browse.model;

import id.unware.poken.domain.Category;
import id.unware.poken.ui.browse.presenter.BrowsePresenter;
import id.unware.poken.ui.browse.presenter.IBrowseModelPresenter;

/**
 * @author Anwar Pasaribu
 * @since Jun 07 2017
 */

public interface IBrowseModel {
    void requestSellerData(IBrowseModelPresenter presenter, int actionId);

    void requestSellerListData(IBrowseModelPresenter presenter);

    void requestMoreProductsByIntentId(IBrowseModelPresenter presenter, int actionId, int page);

    void requestSellerDataByCategory(IBrowseModelPresenter presenter, Category category);

    void requestMoreProductByCategory(IBrowseModelPresenter presenter, Category category, int nextPage);

    void requestMoreSellerData(IBrowseModelPresenter presenter, int page);
}
