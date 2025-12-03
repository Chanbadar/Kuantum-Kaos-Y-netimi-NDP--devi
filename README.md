# Kuantum Ambarı Kontrol Paneli (Quantum Repository Control Panel)

Bu proje, yüksek enerjili kuantum nesnelerinin (Veri Paketleri, Karanlık Madde, Anti Madde) simüle edilmiş bir envanterini yönetmek için geliştirilmiş bir komut satırı uygulamasıdır. Temel amaç, nesnelerin stabilite (kararlılık) seviyelerini takip etmek ve kritik stabilite düşüşlerinde meydana gelebilecek Kuantum Çöküşü riskini modellemektir.

Proje, Nesne Yönelimli Programlama (NYP / OOP) prensiplerini ve özellikle Polimorfizm kavramını farklı programlama dillerinde nasıl uygulayabileceğinizi göstermek için bir alıştırma olarak da hizmet eder.
✨ Temel Özellikler

    Çoklu Dil Desteği: Projenin mantığı C#, Java, JavaScript ve Python dillerinde ayrı ayrı uygulanmıştır.

    Kalıtım ve Polimorfizm: Tüm nesneler (VeriPaketi, KaranlikMadde, AntiMadde), ortak bir temel sınıf olan KuantumNesnesi'nden türetilmiştir.

        AnalizEt() metodu polimorfik olarak her alt sınıfta farklı stabilite düşüşlerine neden olur.

   Kritik Nesne Yönetimi: KaranlikMadde ve AntiMadde gibi kritik nesneler, stabiliteyi hızla artırmak için AcilDurumSogutmasi() metoduna sahiptir.

    Özel İstisnalar: Bir nesnenin stabilitesi sıfıra düştüğünde programı durduran özel bir KuantumCokusuException istisnası tanımlanmıştır.
## Kurulum ve Çalıştırma

Aşağıdaki talimatlar, her bir dildeki uygulamayı çalıştırmanız için gerekli adımları içermektedir:

Python

    Gereksinim: Python 3.x yüklü olmalıdır.

    Çalıştırma: ´python KuantumAmbar.py´
