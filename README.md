# Ateş ve Su – Java Oyunu

Platform temelli bu oyunda amaç, karakterinizi engellerden kaçırarak hedefe ulaştırmak ve canavarlardan kurtulmaktır. Renk değiştirme, ateş etme, zıplama gibi mekanikler içerir.

## 🎮 Kontroller

| Tuş | İşlev |
|-----|-------|
| `1` | Kırmızı renge geç |
| `2` | Mavi renge geç |
| `3` | Magenta renge geç |
| `← / →` | Hareket |
| `↑` | Zıplama |
| `Space` | Ateş et |
| `P` | Oyunu duraklat |
| `K` | Oyunu kaydet |

> ⚠ Canavarlara veya farklı renk tuzaklara değerseniz **ölürsünüz**.

## 🧪 PowerUp'lar

Canavar öldüğünde rastgele PowerUp düşer:

- **Pembe**: Anında oyunu kazandırır.
- **CamGöbeği**: Süre boyunca canavarlara çarparsanız onlar ölür.
- **Kırmızı**: Kırmızı tuzaklara bağışıklık.
- **Mavi**: Mavi tuzaklara bağışıklık.
- **Magenta**: Daha yükseğe zıplarsınız.

> PowerUp etkileri zamanlıdır ve birikimli çalışır.

## 🚀 Çalıştırma

```bash
javac Main.java && java Main
```
## Kayıtlı oyunu açmak için:
```
java Main <dosyaAdi>
# .bin uzantısı otomatik eklenir.
```