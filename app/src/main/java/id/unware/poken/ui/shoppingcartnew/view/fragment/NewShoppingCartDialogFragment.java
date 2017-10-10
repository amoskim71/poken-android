package id.unware.poken.ui.shoppingcartnew.view.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import id.unware.poken.R;
import id.unware.poken.domain.AddressBook;
import id.unware.poken.domain.Product;
import id.unware.poken.domain.ProductImage;
import id.unware.poken.domain.Seller;
import id.unware.poken.domain.Shipping;
import id.unware.poken.domain.ShippingRates;
import id.unware.poken.domain.ShoppingCart;
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.BitmapUtil;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.StringUtils;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.address.view.AddressActivity;
import id.unware.poken.ui.shoppingcartnew.model.NewlyShoppingCartModel;
import id.unware.poken.ui.shoppingcartnew.presenter.NewlyShoppingCartPresenter;
import id.unware.poken.ui.shoppingcartnew.view.INewlyShoppingCartView;


public class NewShoppingCartDialogFragment extends BottomSheetDialogFragment implements INewlyShoppingCartView {

    private static final String TAG = "NewShoppingCartDialogFragment";

    @BindView(R.id.newShoppingCartParentScrollView) NestedScrollView newShoppingCartParentScrollView;

    @BindView(R.id.addressBookIbClose) ImageButton addressBookIbClose;
    @BindView(R.id.newShoppingCartProgressBar) ProgressBar newShoppingCartProgressBar;

    @BindView(R.id.cbSelectAllStoreItem) CheckBox cbSelectAllStoreItem;
    @BindView(R.id.ivStoreAvatar) ImageView ivStoreAvatar;
    @BindView(R.id.tvStoreName) TextView tvStoreName;
    @BindView(R.id.ivProductImage) ImageView ivProductImage;
    @BindView(R.id.tvProductName) TextView tvProductName;
    @BindView(R.id.btnDeleteCartItem) ImageButton btnDeleteCartItem;

    // Shipping address section
    @BindView(R.id.newShoppingCartParentShippingAddress) RelativeLayout newShoppingCartParentShippingAddress;
    @BindView(R.id.newShoppingCartIvShippingAddress) ImageView newShoppingCartIvShippingAddress;
    @BindView(R.id.newShoppingCartTvShipmentAddressTitle) TextView newShoppingCartTvShipmentAddressTitle;
    @BindView(R.id.newShoppingCartTvAddressDetail) TextView newShoppingCartTvAddressDetail;
    @BindView(R.id.newShoppingCartBtnOtherShippingAddress) Button newShoppingCartBtnOtherShippingAddress;

    // Shipment section
    @BindView(R.id.newShoppingCartParentShippingMethod) RelativeLayout newShoppingCartParentShippingMethod;
    @BindView(R.id.newShoppingCartIvShippingMethod) ImageView newShoppingCartIvShippingMethod;
    @BindView(R.id.newShoppingCartTvShipmentMethodTitle) TextView newShoppingCartTvShipmentMethodTitle;
    @BindView(R.id.newShoppingCartSpinnerCourierName) Spinner newShoppingCartSpinnerCourierName;
    @BindView(R.id.newShoppingCartSpinnerCourierServices) Spinner newShoppingCartSpinnerCourierServices;

    // Selected product summary
    @BindView(R.id.newShoppingCartParentProductSummary) LinearLayout newShoppingCartParentProductSummary;

    // Main Button
    @BindView(R.id.newShoppingCartParentMainButton) LinearLayout newShoppingCartParentMainButton;

    // PRODUCT PRICE
    @BindView(R.id.tvProductTotalPrice) TextView tvProductTotalPrice;
    @BindView(R.id.tvPrice2) TextView tvPrice2;
    @BindView(R.id.tvDiscountedPrice) TextView tvDiscountedPrice;
    @BindView(R.id.tvDiscountAmount) TextView tvDiscountAmount;
    @BindView(R.id.viewFlipperProductPrice) ViewFlipper viewFlipperProductPrice;
    @BindView(R.id.ivShippingIcon) ImageView rowCartIvShippingIcon;
    @BindView(R.id.tvSelectedShippingMethodLbl) TextView rowCartTvSelectedShippingMethodLbl;
    @BindView(R.id.tvSelectedShippingMethod) TextView rowCartTvSelectedShippingMethod;

    // ITEM QUANTITY
    @BindView(R.id.parentQuantityControl) CardView parentQuantityControl;
    @BindView(R.id.btnAddQuantity) ImageButton btnAddQuantity;
    @BindView(R.id.btnSubstractQuantity) ImageButton btnSubstractQuantity;
    @BindView(R.id.textItemQuantity) TextView textItemQuantity;

    // Extra note
    @BindView(R.id.rowCartExtraNoteSeparator) View rowCartExtraNoteSeparator;
    @BindView(R.id.rowCartAddNoteIcon) ImageView rowCartAddNoteIcon;
    @BindView(R.id.rowCartAddNoteTextView) TextView rowCartAddNoteTextView;

    // Total fee section
    @BindView(R.id.tvNewShoppingCartTvShippingFee) TextView tvNewShoppingCartTvShippingFee;
    @BindView(R.id.tvNewShoppingCartTotalCost) TextView tvNewShoppingCartTotalCost;

    @BindView(R.id.btnContinueToPayment) Button btnContinueToPayment;
    @BindView(R.id.btnShopMore) Button btnShopMore;

    // List of main button
    @BindViews({R.id.btnContinueToPayment, R.id.btnShopMore}) List<Button> mainButtons;
    @BindViews({R.id.btnAddQuantity, R.id.btnSubstractQuantity}) List<ImageButton> productQuantityCotrollers;

    private Unbinder unbinder;
    private NewlyShoppingCartPresenter presenter;
    private NewShoppingCartDialogListner listener;

    private boolean isShipmentAddressSectionExpanded = false;
    private boolean isShipmentMethodSectionExpanded = false;

    private Product currentProduct;
    private int productQuantity = 1;
    private long selectedCourierId = -1;
    private String selectedCourierService = "";
    private double selectedCourierServiceFee = 0;
    private AddressBook selectedAddressBook;

    private SparseArray<ArrayList<Shipping>> shippingRatesMapping = new SparseArray<>();


    public NewShoppingCartDialogFragment() {}

    public static NewShoppingCartDialogFragment newInstance(Product product) {
        NewShoppingCartDialogFragment f = new NewShoppingCartDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.EXTRA_PRODUCT_DATA, product);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.currentProduct = (Product) getArguments().getSerializable(Constants.EXTRA_PRODUCT_DATA);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_new_shopping_cart_dialog, container, false);

        unbinder = ButterKnife.bind(this, view);

        presenter = new NewlyShoppingCartPresenter(new NewlyShoppingCartModel(), this);

        initView();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.loadLastUsedAddressBook();
    }

    private void initView() {

        // Hide shipping method on row shopping cart
        rowCartIvShippingIcon.setVisibility(View.GONE);
        rowCartTvSelectedShippingMethodLbl.setVisibility(View.GONE);
        rowCartTvSelectedShippingMethod.setVisibility(View.GONE);

        // Hide extra note field
        rowCartExtraNoteSeparator.setVisibility(View.GONE);
        rowCartAddNoteIcon.setVisibility(View.GONE);
        rowCartAddNoteTextView.setVisibility(View.GONE);

        addressBookIbClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        if (this.currentProduct == null) return;

        Seller seller = this.currentProduct.seller;
        if (seller == null) return;

        ArrayList<ProductImage> images = this.currentProduct.images;
        if (images.isEmpty()) return;

        String storeName = seller.store_name;
        String productImage = images.get(0).path;
        String productName = this.currentProduct.name;

        double  productPrice = this.currentProduct.price,
                discountAmount = this.currentProduct.discount_amount,
                discountedPrice = productPrice - ((productPrice * discountAmount) / 100);
        final int productStock = this.currentProduct.stock;

        cbSelectAllStoreItem.setVisibility(View.GONE);

        // Product image thumbnail size
        int imageSize = getContext().getResources().getDimensionPixelSize(R.dimen.clickable_size_64);
        tvStoreName.setText(storeName);
        Picasso.with(getContext())
                .load(productImage)
                .resize(imageSize, imageSize)
                .centerCrop()
                .into(ivProductImage);
        tvProductName.setText(productName);
        tvProductTotalPrice.setText(
                StringUtils.formatCurrency(String.valueOf(productPrice))
        );
        textItemQuantity.setText(
                String.valueOf(1)
        );

        // tvPrice2 to show SALE item
        tvPrice2.setText(StringUtils.formatCurrency(String.valueOf(productPrice)));
        tvPrice2.setPaintFlags(tvPrice2.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);  // Strike
        tvDiscountedPrice.setText(StringUtils.formatCurrency(String.valueOf(discountedPrice)));
        tvDiscountAmount.setText((int) discountAmount + "%");

        // Discount view
        if (discountAmount > 0D) {
            viewFlipperProductPrice.setDisplayedChild(0);
        } else {
            viewFlipperProductPrice.setDisplayedChild(1);
        }

        btnDeleteCartItem.setVisibility(View.GONE);
        cbSelectAllStoreItem.setVisibility(View.GONE);

        // Shopping initial price
        setupTotalPriceView(this.currentProduct, productQuantity);

        // Shipment address section
        newShoppingCartParentShippingAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();

                if (isShipmentAddressSectionExpanded) {
                    // Collapse
                    layoutParams.height = getResources().getDimensionPixelOffset(R.dimen.clickable_size);
                    isShipmentAddressSectionExpanded = false;

                    // Section title of shipping address
                    String selectedAddress = getString(R.string.lbl_select_shipping_address);
                    if (getSelectedAddressBook() != null) {
                        selectedAddress = getSelectedAddressBook().name.concat(" - ").concat(getSelectedAddressBook().address);
                    }
                    newShoppingCartTvShipmentAddressTitle.setText(selectedAddress);

                    // Arrow to bottom
                    newShoppingCartIvShippingAddress.animate().rotation(0F);

                    // Show selected product summary
                    newShoppingCartParentProductSummary.animate().alpha(1F);
                    newShoppingCartParentMainButton.animate().alpha(1F);

                    // Enable shipping method section
                    newShoppingCartParentShippingMethod.setEnabled(true);
                    newShoppingCartParentShippingMethod.animate().alpha(1F);
                    newShoppingCartIvShippingMethod.setColorFilter(BitmapUtil.getEnabledColor(getContext()));

                    // Enable main button
                    for (Button btn : mainButtons) {
                        btn.setEnabled(true);
                    }

                    for (ImageButton ib : productQuantityCotrollers) {
                        ib.setEnabled(true);
                    }

                } else {
                    // Expand
                    layoutParams.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
                    isShipmentAddressSectionExpanded = true;

                    // Section title of shipping address
                    newShoppingCartTvShipmentAddressTitle.setText(R.string.lbl_select_shipping_address);

                    // Arrow to top
                    newShoppingCartIvShippingAddress.animate().rotation(180F);

                    // Hide selected product summary
                    newShoppingCartParentProductSummary.animate().alpha(0.3F);
                    newShoppingCartParentMainButton.animate().alpha(0.3F);

                    // Disable shipping method section
                    newShoppingCartParentShippingMethod.setEnabled(false);
                    newShoppingCartParentShippingMethod.animate().alpha(0.3F);
                    newShoppingCartIvShippingMethod.setColorFilter(BitmapUtil.getDisabledColor(getContext()));


                    // Disable main button
                    for (Button btn : mainButtons) {
                        btn.setEnabled(false);
                    }

                    for (ImageButton ib : productQuantityCotrollers) {
                        ib.setEnabled(false);
                    }
                }

                newShoppingCartParentShippingAddress.setLayoutParams(layoutParams);

            }
        });

        // Shipment method section
        newShoppingCartParentShippingMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();

                if (isShipmentMethodSectionExpanded) {
                    // Collapse
                    layoutParams.height = getResources().getDimensionPixelOffset(R.dimen.clickable_size);
                    isShipmentMethodSectionExpanded = false;

                    // Section title of shipping address
                    String selectedAddress = getString(R.string.lbl_selected_shipping_method);
                    newShoppingCartTvShipmentMethodTitle.setText(selectedAddress);

                    // Arrow to bottom
                    newShoppingCartIvShippingMethod.animate().rotation(0F);

                    // Show selected product summary
                    newShoppingCartParentProductSummary.animate().alpha(1F);
                    newShoppingCartParentMainButton.animate().alpha(1F);

                    // Enable shipping address section
                    newShoppingCartParentShippingAddress.setEnabled(true);
                    newShoppingCartParentShippingAddress.animate().alpha(1F);
                    newShoppingCartIvShippingAddress.setColorFilter(BitmapUtil.getEnabledColor(getContext()));

                    // Enable main button
                    for (Button btn : mainButtons) {
                        btn.setEnabled(true);
                    }

                    for (ImageButton ib : productQuantityCotrollers) {
                        ib.setEnabled(true);
                    }

                } else {
                    // Expand
                    layoutParams.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
                    isShipmentMethodSectionExpanded = true;

                    // Section title
                    newShoppingCartTvShipmentMethodTitle.setText(R.string.lbl_selected_shipping_method);

                    // Arrow to top
                    newShoppingCartIvShippingMethod.animate().rotation(180F);

                    // Hide selected product summary
                    newShoppingCartParentProductSummary.animate().alpha(0.3F);
                    newShoppingCartParentMainButton.animate().alpha(0.3F);

                    // Enable shipping address section
                    newShoppingCartParentShippingAddress.setEnabled(false);
                    newShoppingCartParentShippingAddress.animate().alpha(0.3F);
                    newShoppingCartIvShippingAddress.setColorFilter(BitmapUtil.getDisabledColor(getContext()));

                    // Disable main button
                    for (Button btn : mainButtons) {
                        btn.setEnabled(false);
                    }

                    for (ImageButton ib : productQuantityCotrollers) {
                        ib.setEnabled(false);
                    }
                }

                newShoppingCartParentShippingMethod.setLayoutParams(layoutParams);

            }
        });

        newShoppingCartBtnOtherShippingAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.Log(TAG, "Open shipping address screen.");
                openShippingAddressScreen();
            }
        });

        btnAddQuantity.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        productQuantity = controlItemQuantity(productQuantity, productStock, true);
                        Utils.Log(TAG, "[add] Q: " + productQuantity + ", stok: " + productStock);
                        textItemQuantity.setText(
                                String.valueOf(productQuantity)
                        );

                        // Change shopping cart counter on list page
                        setupTotalPriceView(currentProduct, productQuantity);
                    }
                }
        );

        btnSubstractQuantity.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        productQuantity = controlItemQuantity(productQuantity, productStock, false);
                        Utils.Log(TAG, "[substract] Q: " + productQuantity + ", stok: " + productStock);
                        textItemQuantity.setText(
                                String.valueOf(productQuantity)
                        );

                        // Change shopping cart counter on list page
                        setupTotalPriceView(currentProduct, productQuantity);
                    }
                }
        );

        btnContinueToPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utils.Log(TAG, "Continue to payment");

                if (presenter != null) {
                    presenter.startAddNewShoppingCart(
                            currentProduct.id,
                            productQuantity,
                            selectedCourierId,
                            selectedCourierServiceFee,
                            selectedCourierService,
                            true
                    );
                }

            }
        });

        btnShopMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
                Utils.Log(TAG, "Shop more");

                if (presenter != null) {
                    presenter.startAddNewShoppingCart(
                            currentProduct.id,
                            productQuantity,
                            selectedCourierId,
                            selectedCourierServiceFee,
                            selectedCourierService,
                            false
                    );
                }
            }
        });

    }

    private void openShippingAddressScreen() {
        Intent shippingAddressIntent = new Intent(getContext(), AddressActivity.class);
        startActivityForResult(shippingAddressIntent, Constants.REQ_CODE_ADDRESS_BOOK);
    }

    private void setupTotalPriceView(Product item, int productQuantity) {
        double  productPrice = item.price * productQuantity,
                discountAmount = item.discount_amount,
                discountedPrice = productPrice - ((productPrice * discountAmount) / 100),
                totalPrice = discountedPrice + selectedCourierServiceFee;
        tvNewShoppingCartTvShippingFee.setText(StringUtils.formatCurrency(String.valueOf(selectedCourierServiceFee)));
        tvNewShoppingCartTotalCost.setText(StringUtils.formatCurrency(String.valueOf(totalPrice)));
    }

    private int controlItemQuantity(int currentQuantity, int maxQuantity, boolean isAdd) {
        if (isAdd && currentQuantity < maxQuantity) {
            currentQuantity = currentQuantity + 1;
        } else if (!isAdd && currentQuantity > 1) {
            currentQuantity = currentQuantity - 1;
        }

        return currentQuantity;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Utils.Log(TAG, "Req: " + requestCode + ", res: " + resultCode + ", data: " + data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.REQ_CODE_ADDRESS_BOOK) {
                Utils.Logs('i', TAG, "Address book data found.");
                AddressBook addressBookResult = data.getParcelableExtra(Constants.EXTRA_SELECTED_ADDRESS_BOOK);
                if (addressBookResult != null) {
                    setSelectedAddressBook(addressBookResult);
                    setupSelectedAddressBookView(addressBookResult);
                } else {
                    Utils.Log(TAG, "No parcelable address book found.");
                }
            }
        }
    }

    private void setupSelectedAddressBookView(AddressBook addressBook) {
        newShoppingCartTvShipmentAddressTitle.setText(String.format("%s - %s", addressBook.name, addressBook.address));
        newShoppingCartTvAddressDetail.setText(String.format("%s\n%s\n%s", addressBook.name, addressBook.address, addressBook.phone));
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        dialog.setCancelable(false);

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;

                FrameLayout bottomSheet = d.findViewById(android.support.design.R.id.design_bottom_sheet);

                if (bottomSheet != null) {
                    BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });

        dialog.setTitle("Barang baru di Troli");

        // Do something with your dialog like setContentView() or whatever
        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof NewShoppingCartDialogListner) {
            listener = (NewShoppingCartDialogListner) context;
        } else {
            throw new ClassCastException(context.toString() + " should implement NewShoppingCartDialogListner");
        }
    }

    @Override
    public void onDetach() {
        listener = null;
        super.onDetach();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void setSelectedAddressBook(AddressBook selectedAddressBook) {
        this.selectedAddressBook = selectedAddressBook;
    }

    public AddressBook getSelectedAddressBook() {
        return this.selectedAddressBook;
    }

    @Override
    public void showProductData(Product product) {

    }

    @Override
    public void startAddressBookScreen(long addressBookId) {

    }

    @Override
    public void populateShippingRates(final ArrayList<ShippingRates> shippingRatesArrayList) {

        Utils.Log(TAG, "Shipping rates size: " + shippingRatesArrayList.size());

        ArrayList<String> avaibaleCourier = new ArrayList<>();
        for (ShippingRates item : shippingRatesArrayList) {

            avaibaleCourier.add(item.name);

            // For flat courier tariff
            if (item.courier_rates.isEmpty()) {
                item.courier_rates.add(new Shipping(item.id, String.valueOf(item.name), item.fee));
            }

            shippingRatesMapping.put((int) item.id, item.courier_rates);
        }

        ArrayAdapter<String> cityDataAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, avaibaleCourier);
        cityDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        newShoppingCartSpinnerCourierName.setAdapter(cityDataAdapter);
        newShoppingCartSpinnerCourierName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                selectedCourierId = shippingRatesArrayList.get(pos).id;
                setupCourierServicesByCourierId((int)shippingRatesArrayList.get(pos).id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Utils.Log(TAG, "On nothing selected.");
            }
        });

        // Set first item selected
        newShoppingCartSpinnerCourierName.postDelayed(new Runnable() {
            @Override
            public void run() {
                newShoppingCartSpinnerCourierName.setSelection(0);
            }
        }, 1000);
    }

    private void setupCourierServicesByCourierId(final int courierId) {
        ArrayList<String> courierServiceName = new ArrayList<>();

        for (Shipping item : shippingRatesMapping.get(courierId)) {
            courierServiceName.add(StringUtils.formatCurrency(String.valueOf(item.fee)).concat(" - ").concat(item.name));
        }

        ArrayAdapter<String> cityDataAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, courierServiceName);
        cityDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        newShoppingCartSpinnerCourierServices.setAdapter(cityDataAdapter);
        newShoppingCartSpinnerCourierServices.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                Utils.Log(TAG, "Selected item pos: " + pos + ", id: " + id);
                selectedCourierService = shippingRatesMapping.get(courierId).get(pos).name;
                selectedCourierServiceFee = shippingRatesMapping.get(courierId).get(pos).fee;
                Utils.Log(TAG, "Selected service: " + shippingRatesMapping.get(courierId).get(pos).name);
                Utils.Log(TAG, "Selected service price: " + shippingRatesMapping.get(courierId).get(pos).fee);

                // Update price view
                setupTotalPriceView(currentProduct, productQuantity);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Utils.Log(TAG, "On nothing selected.");
            }
        });

        // Set first item selected
        newShoppingCartSpinnerCourierServices.postDelayed(new Runnable() {
            @Override
            public void run() {
                newShoppingCartSpinnerCourierServices.setSelection(0);
            }
        }, 1000);
    }

    @Override
    public void showFirstShippingAddress(AddressBook addressBook) {
        Utils.Logs('i', TAG, "First address book from server: " + addressBook.name);

        // Set data
        setSelectedAddressBook(addressBook);

        // Setup view
        setupSelectedAddressBookView(addressBook);

        // Load rates istimation after addressbook loaded
        presenter.loadRatesEstimation(this.currentProduct.id, addressBook.id);
    }

    @Override
    public void showNoShippingAddressIndicator(boolean isShippingAddressEmpty) {
        if (isShippingAddressEmpty) {
            newShoppingCartTvAddressDetail.setVisibility(View.GONE);
            newShoppingCartBtnOtherShippingAddress.setText(R.string.btn_add_address_book);
        } else {
            newShoppingCartTvAddressDetail.setVisibility(View.VISIBLE);
            newShoppingCartBtnOtherShippingAddress.setText(R.string.btn_show_address_book);
        }
    }

    @Override
    public void showShoppingPaymentScreen(ShoppingCart shoppingCart) {
        dismissAllowingStateLoss();

        if (listener != null) {
            listener.onContinuePayment(shoppingCart);
        }
    }

    @Override
    public void showPreviousScreen(ShoppingCart shoppingCart) {
        dismissAllowingStateLoss();

        if (listener != null) {
            listener.onContinueShopping();
        }
    }

    @Override
    public void showViewState(UIState uiState) {
        Utils.Logs('w', TAG, "UI State : " + uiState);

        switch (uiState) {
            case LOADING:
                showLoadingIndicator(true);
                break;
            case FINISHED:
                showLoadingIndicator(false);
                break;
            case ERROR:
                showLoadingIndicator(false);
                break;
        }
    }

    private void showLoadingIndicator(boolean isLoading) {
        if (isLoading) {
            newShoppingCartProgressBar.animate().alpha(1F);

            // Disable main button
            for (Button btn : mainButtons) {
                btn.setEnabled(false);
            }

            for (ImageButton ib : productQuantityCotrollers) {
                ib.setEnabled(false);
            }
        } else {
            newShoppingCartProgressBar.animate().alpha(0F);

            // Enable main button
            for (Button btn : mainButtons) {
                btn.setEnabled(true);
            }

            for (ImageButton ib : productQuantityCotrollers) {
                ib.setEnabled(true);
            }
        }
    }

    @Override
    public boolean isActivityFinishing() {
        return getActivity() == null || getActivity().isFinishing();
    }

    public interface NewShoppingCartDialogListner {
        void onContinueShopping();
        void onContinuePayment(ShoppingCart shoppingCart);
    }
}