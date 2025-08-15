package com.server.app.enums;

public class ResultMessages {
    public static final String SUCCESS = "İşlem Başarılı";
    public static final String RECORD_NOT_FOUND = "Kayıt Bulunamadı";
    public static final String PROCESS_FAILED = "İşlem Başarısız";
    public static final String TITLE_OUT_OF_RANGE = "Ünvan 30 karakterden uzun olamaz";
    public static final String WRONG_PHONE_FORMAT = "Geçersiz telefon format";
    public static final String NAME_SURNAME_EXIST = "Bu isim-soyisim kombinasyonu zaten mevcut";
    public static final String EMPTY_NAME = "İsim alanı boş olamaz";
    public static final String EMPTY_SURNAME = "Soyisim alanı boş olamaz";
    public static final String INVALID_BIRTHDATE = "Doğum tarihi gelecekte olamaz";
    public static final String HIRINGDATE_BEFORE_BIRTHDAY = "İşe alım tarihi doğum tarihinden önce olamaz";
    public static final String ORDER_REQUIRED_BEFORE_ORDER_DATE = "Gerekli tarih (required_date) sipariş tarihinden (order_date) önce olamaz";
    public static final String ORDER_SHIPPED_BEFORE_ORDER_DATE  = "Sevk tarihi (shipped_date) sipariş tarihinden (order_date) önce olamaz";
    public static final String NEGATIVE_FREIGHT                 = "Nakliye ücreti (freight) negatif olamaz";
}
