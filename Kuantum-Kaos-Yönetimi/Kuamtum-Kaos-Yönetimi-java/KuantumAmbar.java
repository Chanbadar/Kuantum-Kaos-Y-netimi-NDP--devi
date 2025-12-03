import java.util.*;

/**
 * Kuantum ambarı simülasyonu.
 * Tek dosya halinde: KuantumAmbar.java
 */
public class KuantumAmbar {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<KuantumNesnesi> envanter = new ArrayList<>();
        Random rnd = new Random();

        while (true) {
            System.out.println("\nKUANTUM AMBARI KONTROL PANELİ");
            System.out.println("1. Yeni Nesne Ekle (Rastgele)");
            System.out.println("2. Tüm Envanteri Listele (Durum Raporu)");
            System.out.println("3. Nesneyi Analiz Et (ID isteyerek)");
            System.out.println("4. Acil Durum Soğutması Yap (Sadece IKritik olanlar için!)");
            System.out.println("5. Çıkış");
            System.out.print("Seçiminiz: ");

            String secim = scanner.nextLine().trim();
            try {
                switch (secim) {
                    case "1":
                        // Rastgele tür seç
                        int tip = rnd.nextInt(3); // 0,1,2
                        KuantumNesnesi yeni;
                        String id = UUID.randomUUID().toString();
                        double baslangicStabilite = 30 + rnd.nextDouble() * 70; // 30-100 arası
                        int tehlike;
                        if (tip == 0) { // VeriPaketi
                            tehlike = 1 + rnd.nextInt(3); // 1-3
                            yeni = new VeriPaketi(id, baslangicStabilite, tehlike);
                            System.out.println("Yeni VeriPaketi eklendi. ID: " + id);
                        } else if (tip == 1) { // KaranlikMadde
                            tehlike = 5 + rnd.nextInt(4); // 5-8
                            yeni = new KaranlikMadde(id, baslangicStabilite, tehlike);
                            System.out.println("Yeni KaranlikMadde eklendi. ID: " + id);
                        } else { // AntiMadde
                            tehlike = 8 + rnd.nextInt(3); // 8-10
                            yeni = new AntiMadde(id, baslangicStabilite, tehlike);
                            System.out.println("Yeni AntiMadde eklendi. ID: " + id);
                        }
                        envanter.add(yeni);
                        break;

                    case "2":
                        if (envanter.isEmpty()) {
                            System.out.println("Envanter boş.");
                        } else {
                            System.out.println("Envanter Durum Raporu:");
                            for (KuantumNesnesi n : envanter) {
                                System.out.println(n.DurumBilgisi());
                            }
                        }
                        break;

                    case "3":
                        System.out.print("Analiz edilecek nesnenin ID'sini girin: ");
                        String analizeId = scanner.nextLine().trim();
                        KuantumNesnesi hedef = findById(envanter, analizeId);
                        if (hedef == null) {
                            System.out.println("ID bulunamadı.");
                        } else {
                            try {
                                hedef.AnalizEt();
                                System.out.println("Analiz tamamlandı. Güncel durum: " + hedef.DurumBilgisi());
                            } catch (KuantumCokusuException e) {
                                System.out.println("SİSTEM ÇÖKTÜ! TAHLİYE BAŞLATILIYOR...");
                                System.out.println("Patlayan nesne ID: " + e.getPatlayanId());
                                System.exit(0);
                            }
                        }
                        break;

                    case "4":
                        System.out.print("Soğutulacak nesnenin ID'sini girin: ");
                        String sogutId = scanner.nextLine().trim();
                        KuantumNesnesi sogutHedef = findById(envanter, sogutId);
                        if (sogutHedef == null) {
                            System.out.println("ID bulunamadı.");
                        } else if (sogutHedef instanceof IKritik) {
                            IKritik kritik = (IKritik) sogutHedef;
                            kritik.AcilDurumSogutmasi();
                            System.out.println("Soğutma uygulandı. Güncel durum: " + sogutHedef.DurumBilgisi());
                        } else {
                            System.out.println("Bu nesne soğutulamaz!");
                        }
                        break;

                    case "5":
                        System.out.println("Program sonlandırılıyor.");
                        scanner.close();
                        System.exit(0);
                        break;

                    default:
                        System.out.println("Geçersiz seçenek.");
                }
            } catch (IllegalArgumentException ex) {
                // Örnek: geçersiz parametreyle nesne oluşturma vb.
                System.out.println("Hata: " + ex.getMessage());
            } catch (Exception ex) {
                // Beklenmeyen diğer hatalar - sistem çökmesi sayılmaz
                System.out.println("Beklenmeyen hata: " + ex.getMessage());
            }
        }
    }

    private static KuantumNesnesi findById(List<KuantumNesnesi> list, String id) {
        for (KuantumNesnesi n : list) {
            if (n.getID().equals(id)) return n;
        }
        return null;
    }

    // -------------------------- Sınıf ve arayüz tanımları --------------------------

    // Özel istisna
    static class KuantumCokusuException extends Exception {
        private final String patlayanId;
        public KuantumCokusuException(String id) {
            super("Kuantum çöküşü! Patlayan nesne ID: " + id);
            this.patlayanId = id;
        }
        public String getPatlayanId() { return patlayanId; }
    }

    // Arayüz (sadece kritik olanlar uygular)
    interface IKritik {
        void AcilDurumSogutmasi();
    }

    // Soyut temel sınıf
    static abstract class KuantumNesnesi {
        private final String id;
        private double stabilite; // 0-100
        private int tehlikeSeviyesi; // 1-10

        public KuantumNesnesi(String id, double stabilite, int tehlikeSeviyesi) {
            if (stabilite < 0 || stabilite > 100)
                throw new IllegalArgumentException("Stabilite 0 ile 100 arasında olmalı.");
            if (tehlikeSeviyesi < 1 || tehlikeSeviyesi > 10)
                throw new IllegalArgumentException("TehlikeSeviyesi 1-10 arasında olmalı.");
            this.id = id;
            this.stabilite = stabilite;
            this.tehlikeSeviyesi = tehlikeSeviyesi;
        }

        public String getID() { return id; }
        public double getStabilite() { return stabilite; }
        public int getTehlikeSeviyesi() { return tehlikeSeviyesi; }

        // Korunan yardımcı: stabiliteyi azalt ve 0 veya altına düşerse KuantumCokusuException at
        protected void decreaseStability(double amount) throws KuantumCokusuException {
            this.stabilite -= amount;
            if (this.stabilite <= 0.0) {
                this.stabilite = 0.0;
                throw new KuantumCokusuException(this.id);
            }
        }

        // Korunan yardımcı: stabiliteyi artır (max 100)
        protected void increaseStability(double amount) {
            this.stabilite = Math.min(100.0, this.stabilite + amount);
        }

        // Durum bilgisi
        public String DurumBilgisi() {
            return String.format("ID: %s | Stabilite: %.2f | TehlikeSeviyesi: %d",
                    id, stabilite, tehlikeSeviyesi);
        }

        // Analiz etme metodu (alt sınıflar implement edecek)
        public abstract void AnalizEt() throws KuantumCokusuException;
    }

    // VeriPaketi: sıradan, IKritik değil
    static class VeriPaketi extends KuantumNesnesi {
        public VeriPaketi(String id, double stabilite, int tehlikeSeviyesi) {
            super(id, stabilite, tehlikeSeviyesi);
        }

        @Override
        public void AnalizEt() throws KuantumCokusuException {
            // Stabilite sadece 5 düşer
            System.out.println("Veri içeriği okundu.");
            decreaseStability(5.0);
        }
    }

    // KaranlikMadde: tehlikeli, IKritik uyguluyor
    static class KaranlikMadde extends KuantumNesnesi implements IKritik {
        public KaranlikMadde(String id, double stabilite, int tehlikeSeviyesi) {
            super(id, stabilite, tehlikeSeviyesi);
        }

        @Override
        public void AnalizEt() throws KuantumCokusuException {
            decreaseStability(15.0);
            System.out.println("Karanlık madde analiz edildi.");
        }

        @Override
        public void AcilDurumSogutmasi() {
            increaseStability(50.0);
            System.out.println("Karanlık madde soğutuldu (+50).");
        }
    }

    // AntiMadde: çok tehlikeli, IKritik uyguluyor
    static class AntiMadde extends KuantumNesnesi implements IKritik {
        public AntiMadde(String id, double stabilite, int tehlikeSeviyesi) {
            super(id, stabilite, tehlikeSeviyesi);
        }

        @Override
        public void AnalizEt() throws KuantumCokusuException {
            System.out.println("Evrenin dokusu titriyor...");
            decreaseStability(25.0);
        }

        @Override
        public void AcilDurumSogutmasi() {
            increaseStability(50.0);
            System.out.println("AntiMadde soğutuldu (+50).");
        }
    }
}

