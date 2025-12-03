# 🚀 Kuantum Ambarı Kontrol Paneli (Quantum Repository Control Panel)

Bu proje, yüksek enerjili kuantum nesnelerinin (Veri Paketleri, Karanlık Madde, Anti Madde) **simüle edilmiş** bir envanterini yönetmek için geliştirilmiş bir komut satırı uygulamasıdır.

Temel amaç, nesnelerin **stabilite (kararlılık)** seviyelerini takip etmek ve kritik stabilite düşüşlerinde meydana gelebilecek **Kuantum Çöküşü** riskini modellemektir.

Proje, **Nesne Yönelimli Programlama (NYP / OOP)** prensiplerini ve özellikle **Polimorfizm** kavramını farklı programlama dillerinde nasıl uygulayabileceğinizi göstermek için bir alıştırma olarak da hizmet eder.

---

### ✨ Temel Özellikler

* **Çoklu Dil Desteği:** Projenin mantığı **C#**, **Java**, **JavaScript** ve **Python** dillerinde ayrı ayrı uygulanmıştır.
* **Kalıtım ve Polimorfizm:** Tüm nesneler (`VeriPaketi`, `KaranlikMadde`, `AntiMadde`), ortak bir temel sınıf olan `KuantumNesnesi`'nden türetilmiştir.
* **Kritik Nesne Yönetimi:** Kritik nesneler (`KaranlikMadde` ve `AntiMadde`), stabiliteyi hızla artırmak için özel **`AcilDurumSogutmasi()`** metoduna sahiptir.
* **Özel İstisnalar (Exception Handling):** Stabilite sıfıra düştüğünde programı durduran özel bir `KuantumCokusuException` istisnası tanımlanmıştır.

### 🌐 Dil Dökümü (Technology Stack)

| Dil | Dosya / Dizin | Notlar |
| :--- | :--- | :--- |
| **Python** | `KuantumAmbar.py` | Dinamik tiplendirme ve `hasattr` ile polimorfizm kontrolü. |
| **C\#** | `/CSharp/` | Visual Studio projesi ile arayüzler ve kalıtım. |
| **Java** | `/Java/` | Abstract sınıflar ve arayüzler kullanılarak klasik OOP uygulaması. |
| **JavaScript** | `/JavaScript/` | Node.js ortamında ES6 `class` yapısı kullanılarak simülasyon. |

---

### 🛠️ Kurulum ve Çalıştırma

Lütfen çalıştırmak istediğiniz dilin dizinine gidin ve aşağıdaki komutları kullanın:

#### 🐍 Python

1.  **Gereksinim:** Python 3.x
2.  **Çalıştırma:**
    ```bash
    python KuantumAmbar.py
    ```

#### ☕ Java

1.  **Gereksinim:** JDK
2.  **Çalıştırma:**
    ```bash
    # /Java/ dizininde
    javac KuantumAmbar.java
    java KuantumAmbar
    ```

#### 💻 C\# (.NET)

1.  **Gereksinim:** .NET SDK
2.  **Çalıştırma:**
    ```bash
    # Proje dizininde
    dotnet run
    ```

#### 🟢 JavaScript (Node.js)

1.  **Gereksinim:** Node.js
2.  **Çalıştırma:**
    ```bash
    # /JavaScript/ dizininde
    node KuantumAmbar.js
    ```

---

### 🤝 Katkıda Bulunma

Projenin geliştirilmesine (farklı dillerde implementasyon, yeni özellikler) katkıda bulunmaktan çekinmeyin. Lütfen bir "Issue" açın veya "Pull Request" gönderin.

### 📜 Lisans

Bu proje **AGPL-3 Lisansı** altında lisanslanmıştır.
