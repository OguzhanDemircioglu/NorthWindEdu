package com.server.app.enums;

public class ResultMessages {

    //region General Messages
    public static final String SUCCESS = "Islem Basarili";
    public static final String RECORD_NOT_FOUND = "Kayit Bulunamadi";
    public static final String RECORD_DELETED = "Kayit Silindi";
    public static final String RECORD_UPDATED = "Kayit Güncellendi";
    public static final String PROCESS_FAILED = "Islem Basarisiz";
    public static final String ID_IS_NOT_DELIVERED = "Id İletilmedi";
    public static final String NULL_POINTER_REFERENCE = "Var Olmayan bir değişken girdiniz";
    public static final String VALUES_NOT_MATCHED = "Veri getirilemedi";
    public static final String ILLEGAL_ELEMENT_DELIVERED = "Tip Uyuşmazlığı Oluştu";
    public static final String WRONG_PHONE_FORMAT = "Geçersiz telefon format";
    public static final String SUPPLIER_NOT_FOUND = "Bu ID ile Tedarikçi bulunamadı";
    public static final String CATEGORY_NOT_FOUND = "Bu ID ile Kategori bulunamadı";
    public static final String EMPLOYEE_NOT_FOUND = "Bu ID ile Employee bulunamadı";
    public static final String CUSTOMER_NOT_FOUND = "Bu ID ile Müşteri bulunamadı";
    public static final String SHIPPER_NOT_FOUND = "Bu ID ile Kargocu bulunamadı";
    public static final String REGION_NOT_FOUND = "Bu ID ile Bölge bulunamadı";
    public static final String ORDER_NOT_FOUND = "Bu ID ile Sipariş bulunamadı";
    public static final String PRODUCT_NOT_FOUND = "Bu ID ile Ürün bulunamadı";
    public static final String CUSTOMER_DEMOGRAPHICS_NOT_FOUND = "Bu ID ile Müşteri tipi bulunamadı";
    public static final String TERRITORY_NOT_FOUND = "Bu ID ile Bölge bulunamadı";
    public static final String WRONG_CITY_FORMAT = "Şehir alanı yalnızca harflerden oluşmalı";
    public static final String WRONG_COUNTRY_FORMAT = "Ülke alanı yalnızca harflerden oluşmalı";
    public static final String WRONG_FAX_FORMAT = "Geçersiz fax formatı";
    public static final String WRONG_POSTAL_CODE_FORMAT = "Geçersiz posta kodu formatı";
    //endregion

    //region Employee
    public static final String TITLE_OUT_OF_RANGE = "Ünvan 30 karakterden uzun olamaz";
    public static final String NAME_SURNAME_EXIST = "Bu isim-soyisim kombinasyonu zaten mevcut";
    public static final String EMPTY_NAME = "İsim alanı boş olamaz";
    public static final String EMPTY_SURNAME = "Soyisim alanı boş olamaz";
    public static final String INVALID_BIRTHDATE = "Doğum tarihi gelecekte olamaz";
    public static final String HIRING_DATE_BEFORE_BIRTHDAY = "İşe alım tarihi doğum tarihinden önce olamaz";
    //endregion

    //region Product
    public static final String EMPTY_PRODUCT_STATUS = "Ürünün satış durumu boş olamaz";
    public static final String P_NAME_OUT_OF_RANGE = "Ürün adı 40 karakterden uzun olamaz";
    public static final String QUANTITY_OUT_OF_RANGE = "Ürün miktar bilgisi 20 karakterden uzun olamaz";
    //endregion

    //region Customer
    public static final String EMPTY_CUSTOMER_ID = "Müşteri ID alanı boş bırakılamaz";
    public static final String CONTACT_NAME_OUT_OF_RANGE = "Müşteri ismi 30 karakterden uzun olamaz";
    public static final String ADDRESS_OUT_OF_RANGE = "Adres alanı 30 karakterden uzun olamaz";
    public static final String CITY_OUT_OF_RANGE = "Şehir alanı 30 karakterden uzun olamaz";
    public static final String COUNTRY_OUT_OF_RANGE = "Ülke alanı 15 karakterden uzun olamaz";
    public static final String PHONE_OUT_OF_RANGE = "Telefon alanı 24 haneden uzun olamaz";
    public static final String FAX_OUT_OF_RANGE = "Fax alanı 24 haneden uzun olamaz";
    public static final String REGION_OUT_OF_RANGE = "Bölge alanı 15 karakterden uzun olamaz";
    public static final String POSTAL_CODE_OUT_OF_RANGE = "Posta kodu 10 haneden uzun olamaz";
    public static final String COMPANY_NAME_OUT_OF_RANGE = "Firma ismi 40 karakterden uzun olamaz";
    public static final String C_TITLE_OUT_OF_RANGE = "Unvan 30 karakterden uzun olamaz";

    //endregion

    //region CustomerDemographic
    public static final String EMPTY_CUSTOMER_TYPE_ID = "Müşteri tipi ID alanı boş bırakılamaz";
    //

    //region Territory
    public static final String EMPTY_TERRITORY_ID = "Bölge ID alanı boş veya null olamaz";
    //endregion

    //region Order
    public static final String FREIGHT_NEGATIVE = "Kargo ücreti negatif olamaz";
    public static final String SHIP_NAME_OUT_OF_RANGE = "ShipName 40 karakterden uzun olamaz";
    public static final String SHIP_ADDRESS_OUT_OF_RANGE = "ShipAddress 60 karakterden uzun olamaz";
    public static final String SHIP_CITY_OUT_OF_RANGE = "ShipCity 15 karakterden uzun olamaz";
    public static final String SHIP_REGION_OUT_OF_RANGE = "ShipRegion 15 karakterden uzun olamaz";
    public static final String SHIP_POSTAL_CODE_OUT_OF_RANGE = "ShipPostalCode 10 karakterden uzun olamaz";
    public static final String SHIP_COUNTRY_OUT_OF_RANGE = "ShipCountry 15 karakterden uzun olamaz";
    public static final String REQUIRED_DATE_INVALID = "RequiredDate, OrderDate'ten önce olamaz";
    public static final String SHIPPED_DATE_INVALID = "ShippedDate, OrderDate'ten önce olamaz";
    //endregion

    //region Category
    public static final String C_NAME_OUT_OF_RANGE = "Kategori adı 15 karakterden uzun olamaz";
    //endregion
}