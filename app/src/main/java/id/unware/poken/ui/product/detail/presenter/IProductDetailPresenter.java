package id.unware.poken.ui.product.detail.presenter;

/**
 * @author Anwar Pasaribu
 * @since Jun 06 2017
 */

public interface IProductDetailPresenter {
    void getProductData(long productId);

    void onBuyNow(long productId);
}
