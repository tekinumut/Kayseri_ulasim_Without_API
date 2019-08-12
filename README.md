# Kayseri Ulaşım
Kayseri ulaşım Kayseri için oluşturulmuş bir akıllı şehir mobil uygulamasıdır. API'ler güvenlik açısından gizlenmiştir bu yüzden mevcut kod ile derlenen uygulama çalışmaz.

Uygulamayı APK dosyasını indirerek açabilirsiniz. İndirmek için [tıklayınız.](https://drive.google.com/file/d/1wiElYSwDRwycenYoW6Sco8HQ0gwTaf3q/view)

## Uygulamanın Özellikleri

### Giriş Bölümü

Uygulamanın açılışında gösterilen olan bu bölümden kullanıcı uygulamanın istediği alanlarına erişebilir. Navigation Drawer ile de ekranı soldan sağa çekerek açılacak menüden de bölümlere erişim sağlayabilir.

Vatandaşlar öncelikli olarak bulundukları duraklara gelecek otobüsleri öğrenmek istemektedirler. Bu yüzden kullanıcıyı her defasında sabit bir bilgi için sayfa sayfa dolandırmaktansa ana sayfaya en çok talep edilen bu bilgi eklendi. Böylece uygulama açılır açılmaz kullanıcıya kendisine en yakın(bulunduğu durak) duraktan geçen otobüsler ve gelme süreleri hakkında bilgi verilir.

#### En yakın durak verisinin teknik çalışma algoritması

1. FusedLocationProviderClient metodu ile kullanıcının konumu enlem boylam olarak alınır. 
2. Aldığımız bu enlem ve boylam bilgisi, bize o enlem boylama en yakın mevcut durakları sunucudaki veritabanından dönderecek web servise gönderilir. Burada ayrıca adet sayısı gönderilen konuma en yakın kaç durağın dönderileceğini belirtir. Burada bu sayı 1'dir. Çünkü biz kullanıcıya en yakın durağı döndermek istiyoruz.
3. En yakın durak verisi web servisten alındıktan sonra bu veri aldığı durak id'si karşılığında bize durak hakkında bilgiler veren API'ye yönlendirilir. Bu API ise bize durağa gelecek otobüs bilgilerini sunmaktadır.
4. Tüm bu bilgiler Model'e eklendikten sonra RecyclerView(List)'e yüklenir ve kullanıcıya gösterilir. 
5. Kullanıcının güncel enlem ve boylam bilgisi uygulama tarafından her 5 saniyede bir istenir. Eğer yeni bir enlem ve boylam bilgisi gelmiş ise LifeCycle ile oluşturulmuş bu liste yenilenir. Bu değişikliğin oluşmabilmesi için 30 metre yer değişikliği gerekmektedir. Listenin otomatik yenilenebilmesi için bu listedeki veriler ROOM veritabanına kayıt edilir ve devamlı sistem tarafından konum değişikliği takip edilir. Liste yenilendiğinde **Adım 2**'e geçilir.

![giris](https://user-images.githubusercontent.com/33953921/60799596-1ad9aa80-a17c-11e9-9a0c-fa3c39e69428.png) ![giris2](https://user-images.githubusercontent.com/33953921/60801268-680b4b80-a17f-11e9-88c5-11a5f2c6ccec.png)

### Nasıl Giderim

Kullanıcı bir konum seçer ve o konuma kendi mevcut konumundan hangi otobüs hatları ile gidebilir onu öğrenir. Bu bölüm henüz yapım aşamasındadır. 

### ULAŞIM bölümü

Liste ve Harita adlı iki bölümden oluşur. Otobüs ve duraklar hakkındaki tüm işlemlerin yapıldığı, bilgi alındığı genel bölümdür.

#### Liste 

- Kullanıcının mevcut konumuna en yakın 20 durağın listelendiği bölümdür. Bu duraklar kullanıya en yakın duraktan başlayarak listelenir ve kullanıcının durağa olan uzaklığı metre cinsinden belirtilir.
- Kullanıcı bu duraklara Haritalar uygulamam vb. yöntemler ile yol tarifi alabilir. (Resim 1)
- Kullanıcı bu duraklardan geçecek otobüsleri ve bu otobüslerin ne zaman geleceğine dair bilgi edinebilir. Eğer çok fazla geçecek hat var ise bu hatlar arasında arama yapabilir.(Resim 2)
- Kullanıcı bu duraklardan geçen tüm hatları görebilecek(Sonraki güncelleme ile)
- Kullanıcı durakları favorilerine alabilir ve sadece favori duraklarını görüntüleyebilir. Parlak yıldızlar durakların favorilerde olduğu anlamına gelir.
- Kullanıcı durakta bulunan barkodu telefonunun kamerası ile okutarak o durağın listede görüntülenmesini sağlar.
- Kullanıcı veritabanındaki 6000 durak arasından arama yapabilir. Bu aramada durak no'su veya durak adı veritabanında var ise veri döner. Örneğin: Arama kısmına 10 yazar isek, bize önce eğer varsa durak no'su 10 olan durak döner devamında ise içerisinde 10 kelimesi geçen duraklar döner. (Atatürk Bulvarı 10 caddesi gibi) (Resim 3)

![resim1](https://user-images.githubusercontent.com/33953921/60803108-2086be80-a183-11e9-9dfc-16ac1828bde9.png) ![resim2](https://user-images.githubusercontent.com/33953921/60803109-2086be80-a183-11e9-8bee-3da015148a93.png) ![resim3](https://user-images.githubusercontent.com/33953921/60803110-2086be80-a183-11e9-8de4-85d20339f1ab.png)

#### Harita

- Bu kısımda kullanıcının konumuna değil ekranın orta noktasındaki konuma en yakın 20 durak listelenir.
- Listelenen duraklar ekranın köşe noktaları ile sınırlıdır. Ekran dışında kalan duraklar getirilmez.
- Listelenen durak sayısı 20'den az olabilir ama 20'den fazla olamaz. 
- Favoriye alınmış duraklar yıldız simgesi ile listelenir.
- Durakların üzerine tıklanarak yol tarifi alınabilir, favorilere eklenebilir, geçecek otobüslerin bilgisi edinilebilir.
- Kullanıcı haritayı her hareket ettirdiğinde durak verileri güncellenir.
- Harita gösterim metodu değiştirilebilir.(Uygu görünümü gibi)
- Burada oluşturulan web servisin beklediği veriler : Kullanıcının konumu(enlem ve boylam), ekranın tam orta noktasının konumu ekranın Kuzeydoğu noktasının konumu ve ekranın Güneybatı noktasının konum bilgisidir. 

![harita1](https://user-images.githubusercontent.com/33953921/60803740-8c1d5b80-a184-11e9-9703-28d903198fb7.png)

### Eczaneler

- Kayseri'de bulunan nöbetçi eczanelerin verileri kullanıcıya gösterilir. 
- Eczaneler Gece 1'e kadar veya 24 saat açık olma durumlarına göre farklılık gösterebilir. Bunlar renklendirilmiştir.
- Kullanıcının konumuna göre en yakın eczaneden başlanarak eczaneler listeye eklenir.
- Eczaneler aranabilir ve yol tarifi alınabilir. Yol tarifi daha önce gördüğünüz dialog penceresi ile alınır.

![eczane](https://user-images.githubusercontent.com/33953921/60804004-2a112600-a185-11e9-8b9b-a790b11e26df.png)

### Haberler

Kayseri BüyükŞehir belediyenin paylaştığı haberler kullanıcıya gösterildiği bu bölümde veriler HTMP Parse edilerek alınmıştır. Bunun için jSoup kütüphanesi kullanılmıştır.

Haberin üzerine tıklanarak haber hakkındaki detaylara bir diyalog penceresi aracılığıyla ulaşılabilir.

![haber1](https://user-images.githubusercontent.com/33953921/60804199-a3a91400-a185-11e9-9159-445de520fe00.png) ![haber2](https://user-images.githubusercontent.com/33953921/60804200-a3a91400-a185-11e9-8a3a-633b52fc4f91.png)

### Vefat

- Kayseri'de sisteme kayıt edilen günlük vefat bilgilerinin listelendi bölümdür.
- Geçmiş tarihe ait vefat bilgilerine tarihin üzerine tıklayarak ulaşılabilir.
- Mezarlık veya Taziye adresine yol tarifi alınabilir.
- Geçmiş tarihe ait veriler için  web servise seçilen tarih verisi POST edilir. 

![vefat1](https://user-images.githubusercontent.com/33953921/60804372-11edd680-a186-11e9-980a-9e580d732485.png) ![vefat2](https://user-images.githubusercontent.com/33953921/60804373-11edd680-a186-11e9-8b75-666bc4a2e227.png)

### Otopark

- Kayseri'deki otoparkların konumları gösterilir. 
- Kullanıcıya uzaklıklarına göre listelenir.
- Katlı ve katsız otopark olarak 2'ye ayrılır.
- Tüm otoparklar harita üzerindede gösterilebilir.
- Otoparklara yol tarifi alınabilir.

![otopark1](https://user-images.githubusercontent.com/33953921/60804604-917ba580-a186-11e9-91ff-744559678a7d.png) ![otopark2](https://user-images.githubusercontent.com/33953921/60804605-92143c00-a186-11e9-96a4-1ff91a30299c.png)


