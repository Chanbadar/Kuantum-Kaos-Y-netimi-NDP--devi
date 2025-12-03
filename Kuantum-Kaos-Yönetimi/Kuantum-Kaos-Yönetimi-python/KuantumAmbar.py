#!/usr/bin/env python3
import sys
import uuid
import random
from typing import List

# Özel istisna
class KuantumCokusuException(Exception):
    def __init__(self, patlayan_id: str):
        super().__init__(f"Kuantum çöküşü! Patlayan nesne ID: {patlayan_id}")
        self.patlayan_id = patlayan_id

# Soyut temel sınıf (basitçe temel davranışı sağlar)
class KuantumNesnesi:
    def __init__(self, id: str, stabilite: float, tehlike_seviyesi: int):
        if stabilite < 0.0 or stabilite > 100.0:
            raise ValueError("Stabilite 0 ile 100 arasında olmalı.")
        if tehlike_seviyesi < 1 or tehlike_seviyesi > 10:
            raise ValueError("TehlikeSeviyesi 1-10 arasında olmalı.")
        self._id = id
        self._stabilite = float(stabilite)
        self._tehlike_seviyesi = int(tehlike_seviyesi)

    def getID(self) -> str:
        return self._id

    def getStabilite(self) -> float:
        return self._stabilite

    def getTehlikeSeviyesi(self) -> int:
        return self._tehlike_seviyesi

    # Korunan yardımcılar
    def _decrease_stability(self, amount: float) -> None:
        self._stabilite -= amount
        if self._stabilite <= 0.0:
            self._stabilite = 0.0
            raise KuantumCokusuException(self._id)

    def _increase_stability(self, amount: float) -> None:
        self._stabilite = min(100.0, self._stabilite + amount)

    def DurumBilgisi(self) -> str:
        return f"ID: {self._id} | Stabilite: {self._stabilite:.2f} | TehlikeSeviyesi: {self._tehlike_seviyesi}"

    # Alt sınıflar bunu implement etmeli
    def AnalizEt(self) -> None:
        raise NotImplementedError

# VeriPaketi: sıradan, IKritik değil
class VeriPaketi(KuantumNesnesi):
    def __init__(self, id: str, stabilite: float, tehlike_seviyesi: int):
        super().__init__(id, stabilite, tehlike_seviyesi)

    def AnalizEt(self) -> None:
        print("Veri içeriği okundu.")
        self._decrease_stability(5.0)

# KaranlikMadde: kritik, AcilDurumSogutmasi metodu var
class KaranlikMadde(KuantumNesnesi):
    def __init__(self, id: str, stabilite: float, tehlike_seviyesi: int):
        super().__init__(id, stabilite, tehlike_seviyesi)

    def AnalizEt(self) -> None:
        self._decrease_stability(15.0)
        print("Karanlık madde analiz edildi.")

    def AcilDurumSogutmasi(self) -> None:
        self._increase_stability(50.0)
        print("Karanlık madde soğutuldu (+50).")

# AntiMadde: çok kritik
class AntiMadde(KuantumNesnesi):
    def __init__(self, id: str, stabilite: float, tehlike_seviyesi: int):
        super().__init__(id, stabilite, tehlike_seviyesi)

    def AnalizEt(self) -> None:
        print("Evrenin dokusu titriyor...")
        self._decrease_stability(25.0)

    def AcilDurumSogutmasi(self) -> None:
        self._increase_stability(50.0)
        print("AntiMadde soğutuldu (+50).")

def find_by_id(lst: List[KuantumNesnesi], id: str):
    for n in lst:
        if n.getID() == id:
            return n
    return None

def main():
    envanter: List[KuantumNesnesi] = []
    rnd = random.Random()

    while True:
        print("\nKUANTUM AMBARI KONTROL PANELİ")
        print("1. Yeni Nesne Ekle (Rastgele)")
        print("2. Tüm Envanteri Listele (Durum Raporu)")
        print("3. Nesneyi Analiz Et (ID isteyerek)")
        print("4. Acil Durum Soğutması Yap (Sadece kritik olanlar için!)")
        print("5. Çıkış")
        secim = input("Seçiminiz: ").strip()

        try:
            if secim == "1":
                tip = rnd.randint(0, 2)  # 0,1,2
                yeni_id = str(uuid.uuid4())
                baslangic_stabilite = 30.0 + rnd.random() * 70.0  # 30-100 arası
                if tip == 0:
                    tehlike = 1 + rnd.randint(0, 2)  # 1-3
                    yeni = VeriPaketi(yeni_id, baslangic_stabilite, tehlike)
                    print(f"Yeni VeriPaketi eklendi. ID: {yeni_id}")
                elif tip == 1:
                    tehlike = 5 + rnd.randint(0, 3)  # 5-8
                    yeni = KaranlikMadde(yeni_id, baslangic_stabilite, tehlike)
                    print(f"Yeni KaranlikMadde eklendi. ID: {yeni_id}")
                else:
                    tehlike = 8 + rnd.randint(0, 2)  # 8-10
                    yeni = AntiMadde(yeni_id, baslangic_stabilite, tehlike)
                    print(f"Yeni AntiMadde eklendi. ID: {yeni_id}")
                envanter.append(yeni)

            elif secim == "2":
                if not envanter:
                    print("Envanter boş.")
                else:
                    print("Envanter Durum Raporu:")
                    for n in envanter:
                        print(n.DurumBilgisi())

            elif secim == "3":
                analize_id = input("Analiz edilecek nesnenin ID'sini girin: ").strip()
                hedef = find_by_id(envanter, analize_id)
                if hedef is None:
                    print("ID bulunamadı.")
                else:
                    try:
                        hedef.AnalizEt()
                        print("Analiz tamamlandı. Güncel durum: " + hedef.DurumBilgisi())
                    except KuantumCokusuException as e:
                        print("SİSTEM ÇÖKTÜ! TAHLİYE BAŞLATILIYOR...")
                        print("Patlayan nesne ID: " + e.patlayan_id)
                        sys.exit(0)

            elif secim == "4":
                sogut_id = input("Soğutulacak nesnenin ID'sini girin: ").strip()
                sogut_hedef = find_by_id(envanter, sogut_id)
                if sogut_hedef is None:
                    print("ID bulunamadı.")
                else:
                    # kritik olup olmadığını AcilDurumSogutmasi metodunun varlığı ile kontrol ediyoruz
                    if hasattr(sogut_hedef, "AcilDurumSogutmasi"):
                        sogut_hedef.AcilDurumSogutmasi()
                        print("Soğutma uygulandı. Güncel durum: " + sogut_hedef.DurumBilgisi())
                    else:
                        print("Bu nesne soğutulamaz!")

            elif secim == "5":
                print("Program sonlandırılıyor.")
                sys.exit(0)

            else:
                print("Geçersiz seçenek.")
        except ValueError as ex:
            print("Hata: " + str(ex))
        except Exception as ex:
            # Beklenmeyen diğer hatalar - sistem çökmesi sayılmaz
            print("Beklenmeyen hata: " + str(ex))

if __name__ == "__main__":
    main()

