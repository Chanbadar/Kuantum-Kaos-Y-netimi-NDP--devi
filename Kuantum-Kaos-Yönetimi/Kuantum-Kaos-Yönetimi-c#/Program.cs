using System;
using System.Collections.Generic;

public class KuantumAmbar
{
    public static void Main(string[] args)
    {
        var envanter = new List<KuantumNesnesi>();
        var rnd = new Random();

        while (true)
        {
            Console.WriteLine("\nKUANTUM AMBARI KONTROL PANELİ");
            Console.WriteLine("1. Yeni Nesne Ekle (Rastgele)");
            Console.WriteLine("2. Tüm Envanteri Listele (Durum Raporu)");
            Console.WriteLine("3. Nesneyi Analiz Et (ID ile)");
            Console.WriteLine("4. Acil Durum Soğutması Yap (Sadece IKritik olanlar)");
            Console.WriteLine("5. Çıkış");
            Console.Write("Seçiminiz: ");

            string secim = Console.ReadLine().Trim();

            try
            {
                switch (secim)
                {
                    case "1":
                        int tip = rnd.Next(3);
                        KuantumNesnesi yeni;
                        string id = Guid.NewGuid().ToString();
                        double stabilite = 30 + rnd.NextDouble() * 70;
                        int tehlike;

                        if (tip == 0)
                        {
                            tehlike = rnd.Next(1, 4);
                            yeni = new VeriPaketi(id, stabilite, tehlike);
                            Console.WriteLine("Yeni VeriPaketi eklendi. ID: " + id);
                        }
                        else if (tip == 1)
                        {
                            tehlike = rnd.Next(5, 9);
                            yeni = new KaranlikMadde(id, stabilite, tehlike);
                            Console.WriteLine("Yeni KaranlikMadde eklendi. ID: " + id);
                        }
                        else
                        {
                            tehlike = rnd.Next(8, 11);
                            yeni = new AntiMadde(id, stabilite, tehlike);
                            Console.WriteLine("Yeni AntiMadde eklendi. ID: " + id);
                        }

                        envanter.Add(yeni);
                        break;

                    case "2":
                        if (envanter.Count == 0)
                        {
                            Console.WriteLine("Envanter boş.");
                        }
                        else
                        {
                            Console.WriteLine("Envanter Durum Raporu:");
                            foreach (var n in envanter)
                                Console.WriteLine(n.DurumBilgisi());
                        }
                        break;

                    case "3":
                        Console.Write("Analiz edilecek nesnenin ID'si: ");
                        string analizeId = Console.ReadLine().Trim();
                        var hedef = FindById(envanter, analizeId);

                        if (hedef == null)
                        {
                            Console.WriteLine("ID bulunamadı.");
                        }
                        else
                        {
                            try
                            {
                                hedef.AnalizEt();
                                Console.WriteLine("Analiz tamamlandı. Güncel durum: " + hedef.DurumBilgisi());
                            }
                            catch (KuantumCokusuException ex)
                            {
                                Console.WriteLine("SİSTEM ÇÖKTÜ! TAHLİYE BAŞLATILIYOR...");
                                Console.WriteLine("Patlayan nesne ID: " + ex.PatlayanId);
                                Environment.Exit(0);
                            }
                        }
                        break;

                    case "4":
                        Console.Write("Soğutulacak nesnenin ID'si: ");
                        string sogutId = Console.ReadLine().Trim();
                        var sogutHedef = FindById(envanter, sogutId);

                        if (sogutHedef == null)
                        {
                            Console.WriteLine("ID bulunamadı.");
                        }
                        else if (sogutHedef is IKritik kritik)
                        {
                            kritik.AcilDurumSogutmasi();
                            Console.WriteLine("Soğutma uygulandı. Güncel durum: " + sogutHedef.DurumBilgisi());
                        }
                        else
                        {
                            Console.WriteLine("Bu nesne soğutulamaz!");
                        }
                        break;

                    case "5":
                        Console.WriteLine("Program sonlandırılıyor.");
                        return;

                    default:
                        Console.WriteLine("Geçersiz seçenek.");
                        break;
                }
            }
            catch (ArgumentException ex)
            {
                Console.WriteLine("Hata: " + ex.Message);
            }
            catch (Exception ex)
            {
                Console.WriteLine("Beklenmeyen hata: " + ex.Message);
            }
        }
    }

    private static KuantumNesnesi FindById(List<KuantumNesnesi> list, string id)
    {
        foreach (var n in list)
            if (n.ID == id)
                return n;

        return null;
    }
}

// ---------------------------- Sınıflar ----------------------------

public class KuantumCokusuException : Exception
{
    public string PatlayanId { get; }

    public KuantumCokusuException(string id)
    : base("Kuantum çöküşü! Patlayan nesne ID: " + id)
    {
        PatlayanId = id;
    }
}

public interface IKritik
{
    void AcilDurumSogutmasi();
}

public abstract class KuantumNesnesi
{
    public string ID { get; }
    private double stabilite;
    private int tehlikeSeviyesi;

    public double Stabilite => stabilite;
    public int TehlikeSeviyesi => tehlikeSeviyesi;

    protected KuantumNesnesi(string id, double stabilite, int tehlikeSeviyesi)
    {
        if (stabilite < 0 || stabilite > 100)
            throw new ArgumentException("Stabilite 0-100 arasında olmalı.");

        if (tehlikeSeviyesi < 1 || tehlikeSeviyesi > 10)
            throw new ArgumentException("TehlikeSeviyesi 1-10 arasında olmalı.");

        ID = id;
        this.stabilite = stabilite;
        this.tehlikeSeviyesi = tehlikeSeviyesi;
    }

    protected void DecreaseStability(double amount)
    {
        stabilite -= amount;

        if (stabilite <= 0)
        {
            stabilite = 0;
            throw new KuantumCokusuException(ID);
        }
    }

    protected void IncreaseStability(double amount)
    {
        stabilite = Math.Min(100, stabilite + amount);
    }

    public string DurumBilgisi()
    {
        return $"ID: {ID} | Stabilite: {stabilite:F2} | TehlikeSeviyesi: {tehlikeSeviyesi}";
    }

    public abstract void AnalizEt();
}

public class VeriPaketi : KuantumNesnesi
{
    public VeriPaketi(string id, double stabilite, int tehlikeSeviyesi)
    : base(id, stabilite, tehlikeSeviyesi) { }

    public override void AnalizEt()
    {
        Console.WriteLine("Veri içeriği okundu.");
        DecreaseStability(5);
    }
}

public class KaranlikMadde : KuantumNesnesi, IKritik
{
    public KaranlikMadde(string id, double stabilite, int tehlikeSeviyesi)
    : base(id, stabilite, tehlikeSeviyesi) { }

    public override void AnalizEt()
    {
        DecreaseStability(15);
        Console.WriteLine("Karanlık madde analiz edildi.");
    }

    public void AcilDurumSogutmasi()
    {
        IncreaseStability(50);
        Console.WriteLine("Karanlık madde soğutuldu (+50).");
    }
}

public class AntiMadde : KuantumNesnesi, IKritik
{
    public AntiMadde(string id, double stabilite, int tehlikeSeviyesi)
    : base(id, stabilite, tehlikeSeviyesi) { }

    public override void AnalizEt()
    {
        Console.WriteLine("Evrenin dokusu titriyor...");
        DecreaseStability(25);
    }

    public void AcilDurumSogutmasi()
    {
        IncreaseStability(50);
        Console.WriteLine("AntiMadde soğutuldu (+50).");
    }
}

