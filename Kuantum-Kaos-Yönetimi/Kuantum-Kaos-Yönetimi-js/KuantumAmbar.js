const readline = require("readline");
const { randomUUID } = require("crypto");

const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout
});

// --------------------- Yardımcı ----------------------------------

function soru(s) {
    return new Promise(res => rl.question(s, res));
}

// Özel hata sınıfı
class KuantumCokusuException extends Error {
    constructor(id) {
        super(`Kuantum çöküşü! Patlayan nesne ID: ${id}`);
        this.patlayanId = id;
    }
}

// Arayüz benzeri kullanım (zorunlu değil, JS'de interface yok)

// --------------------- Temel Soyut Sınıf --------------------------

class KuantumNesnesi {
    constructor(id, stabilite, tehlikeSeviyesi) {
        if (stabilite < 0 || stabilite > 100)
            throw new Error("Stabilite 0 ile 100 arasında olmalı.");
        if (tehlikeSeviyesi < 1 || tehlikeSeviyesi > 10)
            throw new Error("TehlikeSeviyesi 1-10 arasında olmalı.");

        this.id = id;
        this.stabilite = stabilite;
        this.tehlikeSeviyesi = tehlikeSeviyesi;
    }

    getID() { return this.id; }
    getStabilite() { return this.stabilite; }
    getTehlikeSeviyesi() { return this.tehlikeSeviyesi; }

    decreaseStability(amount) {
        this.stabilite -= amount;
        if (this.stabilite <= 0) {
            this.stabilite = 0;
            throw new KuantumCokusuException(this.id);
        }
    }

    increaseStability(amount) {
        this.stabilite = Math.min(100, this.stabilite + amount);
    }

    DurumBilgisi() {
        return `ID: ${this.id} | Stabilite: ${this.stabilite.toFixed(2)} | Tehlike: ${this.tehlikeSeviyesi}`;
    }

    // alt sınıflar override eder
    AnalizEt() { }
}

// --------------------- Alt Sınıflar --------------------------

class VeriPaketi extends KuantumNesnesi {
    AnalizEt() {
        console.log("Veri içeriği okundu.");
        this.decreaseStability(5);
    }
}

class KaranlikMadde extends KuantumNesnesi {
    AnalizEt() {
        this.decreaseStability(15);
        console.log("Karanlık madde analiz edildi.");
    }

    AcilDurumSogutmasi() {
        this.increaseStability(50);
        console.log("Karanlık madde soğutuldu (+50).");
    }
}

class AntiMadde extends KuantumNesnesi {
    AnalizEt() {
        console.log("Evrenin dokusu titriyor...");
        this.decreaseStability(25);
    }

    AcilDurumSogutmasi() {
        this.increaseStability(50);
        console.log("AntiMadde soğutuldu (+50).");
    }
}

// --------------------- Yardımcı Fonksiyon --------------------------

function findById(list, id) {
    return list.find(x => x.getID() === id) || null;
}

// --------------------- Ana Program Döngüsü --------------------------

async function main() {
    let envanter = [];

    while (true) {
        console.log("\nKUANTUM AMBARI KONTROL PANELİ");
        console.log("1. Yeni Nesne Ekle (Rastgele)");
        console.log("2. Envanteri Listele");
        console.log("3. Nesneyi Analiz Et (ID)");
        console.log("4. Acil Durum Soğutması (IKritik)");
        console.log("5. Çıkış");

        let secim = (await soru("Seçiminiz: ")).trim();

        try {
            switch (secim) {
                case "1":
                    // Rastgele tür
                    let tip = Math.floor(Math.random() * 3);
                    let id = randomUUID();
                    let stabilite = 30 + Math.random() * 70;
                    let tehlike;
                    let yeni;

                    if (tip === 0) {
                        tehlike = 1 + Math.floor(Math.random() * 3);
                        yeni = new VeriPaketi(id, stabilite, tehlike);
                        console.log("Yeni VeriPaketi eklendi. ID:", id);
                    } else if (tip === 1) {
                        tehlike = 5 + Math.floor(Math.random() * 4);
                        yeni = new KaranlikMadde(id, stabilite, tehlike);
                        console.log("Yeni KaranlikMadde eklendi. ID:", id);
                    } else {
                        tehlike = 8 + Math.floor(Math.random() * 3);
                        yeni = new AntiMadde(id, stabilite, tehlike);
                        console.log("Yeni AntiMadde eklendi. ID:", id);
                    }

                    envanter.push(yeni);
                    break;

                case "2":
                    if (envanter.length === 0) {
                        console.log("Envanter boş.");
                    } else {
                        console.log("Envanter Raporu:");
                        for (let n of envanter) console.log(n.DurumBilgisi());
                    }
                    break;

                case "3":
                    let aid = await soru("ID: ");
                    let hedef = findById(envanter, aid.trim());
                    if (!hedef) {
                        console.log("ID bulunamadı.");
                    } else {
                        try {
                            hedef.AnalizEt();
                            console.log("Analiz tamamlandı:", hedef.DurumBilgisi());
                        } catch (e) {
                            if (e instanceof KuantumCokusuException) {
                                console.log("SİSTEM ÇÖKTÜ! TAHLİYE BAŞLATILIYOR...");
                                console.log("Patlayan ID:", e.patlayanId);
                                process.exit(0);
                            } else {
                                console.log("Hata:", e.message);
                            }
                        }
                    }
                    break;

                case "4":
                    let sid = await soru("ID: ");
                    let sogut = findById(envanter, sid.trim());
                    if (!sogut) {
                        console.log("ID bulunamadı.");
                    } else if (typeof sogut.AcilDurumSogutmasi === "function") {
                        sogut.AcilDurumSogutmasi();
                        console.log("Güncel durum:", sogut.DurumBilgisi());
                    } else {
                        console.log("Bu nesne soğutulamaz!");
                    }
                    break;

                case "5":
                    console.log("Program sonlandırılıyor.");
                    rl.close();
                    process.exit(0);

                default:
                    console.log("Geçersiz seçenek.");
            }
        } catch (ex) {
            console.log("Beklenmeyen hata:", ex.message);
        }
    }
}

main();

