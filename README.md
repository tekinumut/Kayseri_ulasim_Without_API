# Kayseri Ulaşım
Kayseri ulaşım Kayseri için oluşturulmuş bir akıllı şehir mobil uygulamasıdır. API'ler güvenlik açısından gizlenmiştir bu yüzden mevcut kod ile derlenen uygulama çalışmaz.

Uygulamaya Google Play üzerinden erişmek için [tıklayınız.](https://play.google.com/store/apps/details?id=com.Mtkn.kayseri_ulasim)

Uygulamayı APK dosyasını indirerek açabilirsiniz. İndirmek için [tıklayınız.](https://drive.google.com/file/d/1wiElYSwDRwycenYoW6Sco8HQ0gwTaf3q/view)

## Uygulamanın Özellikleri

### Giriş Bölümü

Uygulamanın açılışında gösterilen olan bu bölümden kullanıcı uygulamanın istediği alanlarına erişebilir. Navigation Drawer ile de ekranı soldan sağa çekerek açılacak menüden de bölümlere erişim sağlayabilir.

Vatandaşlar öncelikli olarak bulundukları duraklara gelecek otobüsleri öğrenmek istemektedirler. Bu yüzden kullanıcıyı her defasında sabit bir bilgi için sayfa sayfa dolandırmaktansa, ana sayfaya en çok talep edilen bu bilgi eklendi. Böylece uygulama açılır açılmaz kullanıcıya kendisine en yakın duraktan geçen otobüsler ve bu otobüslerin ne zaman geleceği hakkında bilgi verilir.

#### En yakın durak verisinin teknik çalışma algoritması

1. FusedLocationProviderClient metodu ile kullanıcının konumu enlem boylam olarak alınır. 
2. Aldığımız bu enlem ve boylam bilgisi, bize o enlem boylama en yakın mevcut durakları sunucudaki veritabanından dönderecek web servise gönderilir. Burada ayrıca adet sayısı gönderilen konuma en yakın kaç durağın dönderileceğini belirtir. Burada bu sayı 1'dir. Çünkü biz kullanıcıya en yakın durağı döndermek istiyoruz.
3. En yakın durak verisi web servisten alındıktan sonra bu veri aldığı durak id'si karşılığında bize durak hakkında bilgiler veren API'ye yönlendirilir. Bu API ise bize durağa gelecek otobüs bilgilerini sunmaktadır.
4. Tüm bu bilgiler Model'e eklendikten sonra RecyclerView(List)'e yüklenir ve kullanıcıya gösterilir. 
5. Kullanıcının güncel enlem ve boylam bilgisi uygulama tarafından her 5 saniyede bir istenir. Eğer yeni bir enlem ve boylam bilgisi gelmiş ise LifeCycle ile oluşturulmuş bu liste yenilenir. Bu değişikliğin oluşmabilmesi için 30 metre yer değişikliği gerekmektedir. Listenin otomatik yenilenebilmesi için bu listedeki veriler ROOM veritabanına kayıt edilir ve devamlı sistem tarafından konum değişikliği takip edilir. Liste yenilendiğinde **Adım 2**'e geçilir.

![1 1main](https://user-images.githubusercontent.com/33953921/63161021-cf8ea380-c027-11e9-9cce-5a12df331488.png) ![1 2mainPageNavigation](https://user-images.githubusercontent.com/33953921/63161022-d0273a00-c027-11e9-95a0-58b672858563.png)

### Nasıl Giderim

Kullanıcılar Kayseri sınırları içerisinde bir konumdan başka bir konuma hangi alternatif hatlar ile gidebileceğinin bilgisinin sunulduğu bölümdür. 

- Kullanıcı ekrana basılı tutarak başlangıç ve gidiş noktası seçer. 
- Kullanıcıya seçtiği noktalar arasında giden hatlar liste olarak sunulur.
- Baz alınacak duraklar: Başlangıç noktasının 500 metre çapında ve gidiş noktasının 1 km çapındaki duraklardır.
- Yürüme mesafesi 1500 metre üzerinde olamaz. 
- Çizilen rota: Mevcut konumunuzdan ilk durağa. İlk duraktan son durağa. Son duraktan gidiş noktanıza.


![2 1howToGoRecycler](https://user-images.githubusercontent.com/33953921/63161023-d0273a00-c027-11e9-9510-9f089a9adf09.png) ![2 2howToGoMap](https://user-images.githubusercontent.com/33953921/63161024-d0273a00-c027-11e9-992c-3a0cfa5b506a.png)

### ULAŞIM bölümü

Liste, Harita ve Hatlar adlı üç bölümden oluşur. Otobüs, duraklar ve hatlar hakkındaki tüm işlemlerin yapıldığı, bilgi alındığı genel bölümdür.

#### Liste 

- Kullanıcının mevcut konumuna en yakın 20 durağın listelendiği bölümdür. Bu duraklar kullanıya en yakın duraktan başlayarak listelenir ve kullanıcının durağa olan uzaklığı metre cinsinden belirtilir.
- Kullanıcı bu duraklara Haritalar uygulamam vb. yöntemler ile yol tarifi alabilir.
- Kullanıcı bu duraklardan geçecek otobüsleri ve bu otobüslerin ne zaman geleceğine dair bilgi edinebilir. Eğer çok fazla geçecek hat var ise bu hatlar arasında arama yapabilir.
- Kullanıcı bu duraklardan geçen tüm hatları görebilir.
- Duraktan geçen hatlar arasında arama yapabilir. Hat hakkında detaylı bilgileri alabilir.
- Kullanıcı durakları favorilerine alabilir ve sadece favori duraklarını görüntüleyebilir. Parlak yıldızlar durakların favorilerde olduğu anlamına gelir.
- Kullanıcı durakta bulunan barkodu telefonunun kamerası ile okutarak o durağın listede görüntülenmesini sağlar.
- Kullanıcı veritabanındaki 6000 durak arasından arama yapabilir. Bu aramada durak no'su veya durak adı veritabanında var ise veri döner. Örneğin: Arama kısmına 10 yazar isek, bize önce eğer varsa durak no'su 10 olan durak döner devamında ise içerisinde 10 kelimesi geçen duraklar döner. (Atatürk Bulvarı 10 caddesi gibi)

![3 1ulasim](https://user-images.githubusercontent.com/33953921/63161028-d0bfd080-c027-11e9-9275-96b495c4fa39.png) ![3 1 1ulasim](https://user-images.githubusercontent.com/33953921/63161025-d0273a00-c027-11e9-8f76-4aed7675d477.png)

![3 1 2ulasim](https://user-images.githubusercontent.com/33953921/63161026-d0273a00-c027-11e9-9db5-a803351df463.png) ![3 1 3ulasim](https://user-images.githubusercontent.com/33953921/63161027-d0bfd080-c027-11e9-898f-c487f11babc6.png)

#### Harita

- Bu kısımda kullanıcının konumuna değil ekranın orta noktasındaki konuma en yakın 20 durak listelenir.
- Listelenen duraklar ekranın köşe noktaları ile sınırlıdır. Ekran dışında kalan duraklar getirilmez.
- Listelenen durak sayısı 20'den az olabilir ama 20'den fazla olamaz. 
- Favoriye alınmış duraklar yıldız simgesi ile listelenir.
- Durakların üzerine tıklanarak yol tarifi alınabilir, favorilere eklenebilir, geçecek otobüslerin bilgisi edinilebilir.
- Kullanıcı haritayı her hareket ettirdiğinde durak verileri güncellenir.
- Harita gösterim metodu değiştirilebilir.(Uygu görünümü gibi)
- Burada oluşturulan web servisin beklediği veriler : Kullanıcının konumu(enlem ve boylam), ekranın tam orta noktasının konumu ekranın Kuzeydoğu noktasının konumu ve ekranın Güneybatı noktasının konum bilgisidir. 

![3 2ulasim](https://user-images.githubusercontent.com/33953921/63161029-d0bfd080-c027-11e9-8860-2b224ea756b5.png)

#### Hatlar

- Bu kısımda kullanıcılar hatlar hakkında detaylı bilgi alır.
- İlk olarak konumuna en yakın 5 duraktan geçen hatlar listelenir.
- Hattın geçtiği tüm duraklar harita üzerinden görüntülenebilir.
- Hattan geçen otobüslerin mevcut konumu eğer veri var ise haritada görüntülenecektir.
- Otobüslerin mevcut konum verisi devamlı güncellenmemektedir. İleride MVVM pattern ile otobüsler canlı olarak takip edilebilir.

![3 3ulasim](https://user-images.githubusercontent.com/33953921/63161031-d1586700-c027-11e9-9d07-da249df48d8b.png) ![3 3 2ulasim](https://user-images.githubusercontent.com/33953921/63161030-d1586700-c027-11e9-86b1-66785820111b.png)

### Eczaneler

- Kayseri'de bulunan nöbetçi eczanelerin verileri kullanıcıya gösterilir. 
- Eczaneler Gece 1'e kadar veya 24 saat açık olma durumlarına göre farklılık gösterebilir. Bunlar renklendirilmiştir.
- Kullanıcının konumuna göre en yakın eczaneden başlanarak eczaneler listeye eklenir.
- Eczaneler aranabilir ve yol tarifi alınabilir. Yol tarifi daha önce gördüğünüz dialog penceresi ile alınır.

![4 1eczane](https://user-images.githubusercontent.com/33953921/63161032-d0bfd080-c027-11e9-9f07-307f3e1867a1.png)

### Vefat

- Kayseri'de sisteme kayıt edilen günlük vefat bilgilerinin listelendi bölümdür.
- Geçmiş tarihe ait vefat bilgilerine tarihin üzerine tıklayarak ulaşılabilir.
- Mezarlık veya Taziye adresine yol tarifi alınabilir.
- Geçmiş tarihe ait veriler için  web servise seçilen tarih verisi POST edilir. 

![5 1Vefat](https://user-images.githubusercontent.com/33953921/63161034-d1586700-c027-11e9-973f-c00c6070c718.png) ![5 2Vefat](https://user-images.githubusercontent.com/33953921/63161035-d1586700-c027-11e9-9aed-7bee97ffc521.png)

### Otopark

- Kayseri'deki otoparkların konumları gösterilir. 
- Kullanıcıya uzaklıklarına göre listelenir.
- Katlı ve katsız otopark olarak 2'ye ayrılır.
h- Otoparklara yol tarifi alınabilir.

![6 1Otopark](https://user-images.githubusercontent.com/33953921/63161036-d1f0fd80-c027-11e9-9b86-b5277a9ad718.png) ![6 2Otopark](https://user-images.githubusercontent.com/33953921/63161490-e2ee3e80-c028-11e9-8950-db3c6f22b3be.png)

### KAYBİS

- Kayseri'deki bisiklet noktalarının konumları gösterilir.
- Bisiklet noktaları kullanıcının mevcut konumuna göre listelenmeye başlar.
- Bisiklet istasyonlarının doluluk oranları listede belirtilir.
- Doluluk oranı; İstasyondaki mevcut bisiklet sayısı / İstasyondaki toplam bisiklet yuvası şeklinde gösterilir.
- Eğer istasyon boş ise liste kırmızı, doluluk oranı %20 ve altındaysa turunca üstünde ise yeşil olarak renklendirilir.
- İstasyonlara yol tarifi alınabilir.
- Tüm istasyonlar harita üzerinde de görüntülenebilir.

![7 1Bisiklet](https://user-images.githubusercontent.com/33953921/63161037-d1f0fd80-c027-11e9-988e-ebf4daf84a61.png) ![7 2Bisiklet](https://user-images.githubusercontent.com/33953921/63161038-d1f0fd80-c027-11e9-8c10-12e04d869981.png)

### Haberler

Kayseri BüyükŞehir belediyenin paylaştığı haberler kullanıcıya gösterildiği bu bölümde veriler HTMP Parse edilerek alınmıştır. Bunun için jSoup kütüphanesi kullanılmıştır.

Haberin üzerine tıklanarak haber hakkındaki detaylara bir diyalog penceresi aracılığıyla ulaşılabilir.

![8 1Haberler](https://user-images.githubusercontent.com/33953921/63161039-d1f0fd80-c027-11e9-8154-d663339a13b5.png) ![8 2Haberler](https://user-images.githubusercontent.com/33953921/63161040-d1f0fd80-c027-11e9-8130-146bdc273881.png)





