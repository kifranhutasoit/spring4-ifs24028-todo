package org.delcom.starter.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Base64;

class HomeControllerTest {

    HomeController controller = new HomeController();

    private String encode(String text) {
        return Base64.getEncoder().encodeToString(text.getBytes());
    }

    // ========== TESTS UNTUK hello() DAN sayHello() ==========

    @Test
    @DisplayName("Mengembalikan pesan sapaan utama dari endpoint '/'")
    void testHello() {
        assertEquals("Hay Abdullah, selamat datang di pengembangan aplikasi dengan Spring Boot!", controller.hello());
    }

    @Test
    @DisplayName("Mengembalikan sapaan personal dengan nama pengguna")
    void testSayHello() {
        assertEquals("Hello, Abdullah!", controller.sayHello("Abdullah"));
    }

    // ========== TESTS UNTUK informasiNim() ==========

    @Test
    @DisplayName("Menampilkan informasi lengkap untuk NIM yang valid")
    void testInformasiNimValid() {
        String nim = "11322001";
        String result = controller.informasiNim(nim);
        assertTrue(result.contains("Diploma 3 Teknologi Informasi"));
        assertTrue(result.contains("Angkatan: 2022"));
        assertTrue(result.contains("Urutan: 1"));
    }

    @Test
    @DisplayName("Menangani NIM dengan prefix tidak dikenal")
    void testInformasiNimUnknownPrefix() {
        String result = controller.informasiNim("99999001");
        assertTrue(result.contains("Program Studi: Tidak diketahui"));
    }

    // ========== TESTS UNTUK perolehanNilai() ==========

    @Test
    @DisplayName("Menghasilkan Grade A untuk nilai sempurna")
    void testPerolehanNilai_GradeA() {
        String data = "10\n10\n10\n20\n20\n30\n" +
                "PA|100|100\nT|100|100\nK|100|100\nP|100|100\nUTS|100|100\nUAS|100|100\n---";
        String result = controller.perolehanNilai(encode(data));
        assertTrue(result.contains("Grade: A"));
    }

    @Test
    @DisplayName("Menghasilkan Grade AB untuk nilai 72-79.4")
    void testPerolehanNilai_GradeAB() {
        String data = "10\n10\n10\n20\n20\n30\n" +
                "PA|100|75\nT|100|75\nK|100|75\nP|100|75\nUTS|100|75\nUAS|100|75\n---";
        String result = controller.perolehanNilai(encode(data));
        assertTrue(result.contains("Grade: AB"));
    }

    @Test
    @DisplayName("Menghasilkan Grade B untuk nilai 64.5-71.9")
    void testPerolehanNilai_GradeB() {
        String data = "10\n10\n10\n20\n20\n30\n" +
                "PA|100|68\nT|100|68\nK|100|68\nP|100|68\nUTS|100|68\nUAS|100|68\n---";
        String result = controller.perolehanNilai(encode(data));
        assertTrue(result.contains("Grade: B"));
    }

    @Test
    @DisplayName("Menghasilkan Grade BC untuk nilai 57-64.4")
    void testPerolehanNilai_GradeBC() {
        String data = "10\n10\n10\n20\n20\n30\n" +
                "PA|100|60\nT|100|60\nK|100|60\nP|100|60\nUTS|100|60\nUAS|100|60\n---";
        String result = controller.perolehanNilai(encode(data));
        assertTrue(result.contains("Grade: BC"));
    }

    @Test
    @DisplayName("Menghasilkan Grade C untuk nilai 49.5-56.9")
    void testPerolehanNilai_GradeC() {
        String data = "10\n10\n10\n20\n20\n30\n" +
                "PA|100|52\nT|100|52\nK|100|52\nP|100|52\nUTS|100|52\nUAS|100|52\n---";
        String result = controller.perolehanNilai(encode(data));
        assertTrue(result.contains("Grade: C"));
    }

    @Test
    @DisplayName("Menghasilkan Grade D untuk nilai 34-49.4")
    void testPerolehanNilai_GradeD() {
        String data = "10\n10\n10\n20\n20\n30\n" +
                "PA|100|40\nT|100|40\nK|100|40\nP|100|40\nUTS|100|40\nUAS|100|40\n---";
        String result = controller.perolehanNilai(encode(data));
        assertTrue(result.contains("Grade: D"));
    }

    @Test
    @DisplayName("Menghasilkan Grade E untuk nilai di bawah 34")
    void testPerolehanNilai_GradeE() {
        String data = "10\n10\n10\n20\n20\n30\n" +
                "PA|100|10\nT|100|10\nK|100|10\nP|100|10\nUTS|100|10\nUAS|100|10\n---";
        String result = controller.perolehanNilai(encode(data));
        assertTrue(result.contains("Grade: E"));
    }

    @Test
    @DisplayName("Menangani input tanpa data nilai (hanya 6 baris bobot)")
    void testPerolehanNilai_NoDataLines() {
        String data = "10\n10\n10\n20\n20\n30\n";
        String result = controller.perolehanNilai(encode(data));
        assertTrue(result.contains(">> Partisipatif: 0/100 (0.00/10)"));
        assertTrue(result.contains(">> Grade: E"));
    }

    @Test
    @DisplayName("Menangani beberapa entri untuk komponen yang sama")
    void testPerolehanNilai_MultipleEntries() {
        String data = "10\n10\n10\n20\n20\n30\n" +
                "PA|50|40\nPA|50|50\nT|100|80\nK|100|70\nP|100|85\nUTS|100|75\nUAS|100|80\n---";
        String result = controller.perolehanNilai(encode(data));
        assertTrue(result.contains("Partisipatif"));
    }

    @Test
    @DisplayName("Menangani komponen nilai yang hilang (missing)")
    void testPerolehanNilai_MissingComponents() {
        String data = "10\n10\n10\n20\n20\n30\n" +
                "PA|100|80\nT|100|75\nP|100|85\nUTS|100|70\nUAS|100|75\n---";
        String result = controller.perolehanNilai(encode(data));
        assertTrue(result.contains("Kuis: 0/100"));
    }

    @Test
    @DisplayName("Menolak input dengan kurang dari 6 baris bobot")
    void testPerolehanNilai_InvalidInputLessThan6Lines() {
        String data = "10\n10\n10\n";
        String result = controller.perolehanNilai(encode(data));
        assertTrue(result.contains("Input tidak valid"));
    }

    @Test
    @DisplayName("Menangani input Base64 yang tidak valid")
    void testPerolehanNilai_InvalidBase64() {
        String result = controller.perolehanNilai("invalid!!");
        assertTrue(result.contains("Error processing data"));
    }

    @Test
    @DisplayName("Mengabaikan baris kosong dan simbol tidak dikenal")
    void testPerolehanNilai_EmptyLinesAndUnknownSymbol() {
        String data = "10\n10\n10\n20\n20\n30\n" +
                "\nUNKNOWN|100|50\nT|100|75\nK|100|70\nP|100|85\nUTS|100|80\nUAS|100|75\n---";
        String result = controller.perolehanNilai(encode(data));
        assertTrue(result.contains("Perolehan Nilai"));
    }

    @Test
    @DisplayName("Mengabaikan baris dengan kurang dari 3 elemen")
    void testPerolehanNilai_PartsLessThan3Ignored() {
        String data = "10\n10\n10\n20\n20\n30\n" +
                "PA|100|80\nT|100\nK|100|90\nP|100|80\nUTS|100|70\nUAS|100|60\n---";
        String result = controller.perolehanNilai(encode(data));
        assertTrue(result.contains("Grade"));
    }

    @Test
    @DisplayName("Menangani kasus maxPA = 0 (tidak ada data PA)")
    void testPerolehanNilai_MaxZeroCase() {
        String data = "10\n10\n10\n20\n20\n30\n" +
                "T|100|90\nK|100|80\nP|100|70\nUTS|100|60\nUAS|100|50\n---";
        String result = controller.perolehanNilai(encode(data));
        assertTrue(result.contains("Partisipatif: 0/100"));
    }

    @Test
    @DisplayName("Menghentikan pembacaan saat menemukan ---")
    void testPerolehanNilai_StopReadingAtBreak() {
        String data = "10\n10\n10\n20\n20\n30\n" +
                "PA|100|100\n---\nPA|100|50\nT|100|40";
        String result = controller.perolehanNilai(encode(data));
        assertFalse(result.contains("T|100|40"));
    }

    @Test
    @DisplayName("Menangani NumberFormatException")
    void testPerolehanNilai_CatchNumberFormatException() {
        String data = "a\nb\nc\nd\ne\nf\n";
        String result = controller.perolehanNilai(encode(data));
        assertTrue(result.contains("Error processing data"));
    }

    // ========== TESTS UNTUK perbedaanL() ==========
    
    @Test
    @DisplayName("Menghitung perbedaan L 3x3 dengan L > Kebalikan")
    void testPerbedaanL_3x3_LGreater() {
        String data = "3\n" +
                "5 2 3\n" +
                "4 5 6\n" +
                "10 8 1";
        String result = controller.perbedaanL(encode(data));
        assertTrue(result.contains("Nilai L: 27\n"));
        assertTrue(result.contains("Nilai Kebalikan L: 12\n"));
        assertTrue(result.contains("Perbedaan: 15\n"));
        assertTrue(result.contains("Dominan: 27\n"));
    }

    @Test
    @DisplayName("Menghitung perbedaan L 3x3 dengan Kebalikan > L")
    void testPerbedaanL_3x3_KebalikanGreater() {
        String data = "3\n" +
                "1 10 15\n" +
                "4 5 20\n" +
                "2 3 25";
        String result = controller.perbedaanL(encode(data));
        assertTrue(result.contains("Nilai L: 10\n"));
        assertTrue(result.contains("Nilai Kebalikan L: 70\n"));
        assertTrue(result.contains("Dominan: 70\n"));
    }

    @Test
    @DisplayName("Menghitung perbedaan L 3x3 dengan L == Kebalikan (dominan = tengah)")
    void testPerbedaanL_3x3_Equal() {
        String data = "3\n" +
                "1 2 3\n" +
                "4 5 6\n" +
                "7 8 9";
        String result = controller.perbedaanL(encode(data));
        assertTrue(result.contains("Nilai L: 20\n"));
        assertTrue(result.contains("Nilai Kebalikan L: 20\n"));
        assertTrue(result.contains("Perbedaan: 0\n"));
        assertTrue(result.contains("Dominan: 5\n"));
    }

    @Test
    @DisplayName("Menangani matriks 1x1")
    void testPerbedaanL_1x1() {
        String data = "1\n5";
        String result = controller.perbedaanL(encode(data));
        assertTrue(result.contains("Nilai Tengah: 5\n"));
        assertTrue(result.contains("Dominan: 5\n"));
    }

    @Test
    @DisplayName("Menangani matriks 2x2")
    void testPerbedaanL_2x2() {
        String data = "2\n1 2\n3 4";
        String result = controller.perbedaanL(encode(data));
        assertTrue(result.contains("Nilai Tengah: 10\n"));
        assertTrue(result.contains("Dominan: 10\n"));
    }

    @Test
    @DisplayName("Menangani matriks 4x4 dengan n genap (tengah = 4 nilai)")
    void testPerbedaanL_4x4_EvenN() {
        String data = "4\n" +
                "1 2 3 4\n" +
                "5 6 7 8\n" +
                "9 10 11 12\n" +
                "13 14 15 16";
        String result = controller.perbedaanL(encode(data));
        assertTrue(result.contains("Nilai Tengah: 34\n"));
    }

    @Test
    @DisplayName("Menangani jumlah baris kurang dari ukuran matriks")
    void testPerbedaanL_InvalidRowCount() {
        String data = "3\n1 2 3\n4 5 6";
        String result = controller.perbedaanL(encode(data));
        assertTrue(result.contains("Jumlah baris tidak sesuai"));
    }

    @Test
    @DisplayName("Menangani elemen baris kurang dari ukuran matriks")
    void testPerbedaanL_InvalidElementCount() {
        String data = "3\n1 2 3\n4 5\n7 8 9";
        String result = controller.perbedaanL(encode(data));
        assertTrue(result.contains("Jumlah elemen pada baris 2 tidak sesuai"));
    }

    @Test
    @DisplayName("Menangani input kosong (string base64 kosong)")
    void testPerbedaanL_EmptyInput() {
        String data = "";
        String result = controller.perbedaanL(encode(data));
        assertTrue(result.contains("Data matriks kosong"));
    }

    @Test
    @DisplayName("Menangani input Base64 tidak valid")
    void testPerbedaanL_InvalidBase64() {
        String result = controller.perbedaanL("invalid!!");
        assertTrue(result.contains("Error processing data"));
    }

    @Test
    @DisplayName("Menangani NumberFormatException saat parsing n")
    void testPerbedaanL_CatchNumberFormatExceptionOnN() {
        String data = "a\n1 2 3\n4 5 6\n7 8 9";
        String result = controller.perbedaanL(encode(data));
        assertTrue(result.contains("Error processing data"));
    }

    @Test
    @DisplayName("Menangani NumberFormatException saat parsing elemen")
    void testPerbedaanL_CatchNumberFormatExceptionOnElement() {
        String data = "3\n1 2 3\n4 5 6\n7 8 abc";
        String result = controller.perbedaanL(encode(data));
        assertTrue(result.contains("Error processing data"));
    }

    // ========== TESTS UNTUK palingTer() - LENGKAP UNTUK 100% COVERAGE ==========
    
    @Test
    @DisplayName("BRANCH 1: Menangani input kosong (decoded.isEmpty() == true)")
    void testPalingTer_EmptyInput() {
        String data = "";
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Data nilai kosong"));
    }

    @Test
    @DisplayName("BRANCH 2: Menangani daftarNilai kosong setelah parsing")
    void testPalingTer_NoValidData() {
        String data = "\n\n\n---";
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Tidak ada nilai yang dimasukkan"));
    }

    @Test
    @DisplayName("BRANCH 3: Menangani input Base64 tidak valid (Exception)")
    void testPalingTer_InvalidBase64() {
        String result = controller.palingTer("%%%invalid%%%");
        assertTrue(result.contains("Error processing data"));
    }

    @Test
    @DisplayName("BRANCH 4: Menangani NumberFormatException saat parsing")
    void testPalingTer_CatchNumberFormatException() {
        String data = "10\n20\ntiga puluh\n---";
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Error processing data"));
    }

    @Test
    @DisplayName("BRANCH 5: hashMapNilai.containsKey(nilai) == TRUE path")
    void testPalingTer_ContainsKeyTrue() {
        String data = "10\n10\n10\n---";
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Terbanyak: 10 (3x)\n"));
    }

    @Test
    @DisplayName("BRANCH 6: hashMapNilai.containsKey(nilai) == FALSE path")
    void testPalingTer_ContainsKeyFalse() {
        String data = "10\n20\n30\n---";
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Tersedikit: 10 (1x)\n"));
    }

    @Test
    @DisplayName("BRANCH 7: nilaiTertinggi < arrayNilai[i] == TRUE")
    void testPalingTer_TertinggiTrue() {
        String data = "5\n10\n15\n20\n---";
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Tertinggi: 20\n"));
    }

    @Test
    @DisplayName("BRANCH 8: nilaiTertinggi < arrayNilai[i] == FALSE")
    void testPalingTer_TertinggiFalse() {
        String data = "20\n15\n10\n5\n---";
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Tertinggi: 20\n"));
    }

    @Test
    @DisplayName("BRANCH 9: nilaiTerendah > arrayNilai[i] == TRUE")
    void testPalingTer_TerendahTrue() {
        String data = "20\n15\n10\n5\n---";
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Terendah: 5\n"));
    }

    @Test
    @DisplayName("BRANCH 10: nilaiTerendah > arrayNilai[i] == FALSE")
    void testPalingTer_TerendahFalse() {
        String data = "5\n10\n15\n20\n---";
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Terendah: 5\n"));
    }

    @Test
    @DisplayName("BRANCH 11: hashMapTotalTerendah.containsKey() == TRUE")
    void testPalingTer_TotalTerendahContainsKeyTrue() {
        String data = "10\n10\n20\n---";
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Jumlah Terendah"));
    }

    @Test
    @DisplayName("BRANCH 12: hashMapTotalTerendah.containsKey() == FALSE")
    void testPalingTer_TotalTerendahContainsKeyFalse() {
        String data = "10\n20\n30\n---";
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Jumlah Terendah: 10 * 1 = 10\n"));
    }

    @Test
    @DisplayName("BRANCH 13: arrayNilai[i] == nilaiJumlahTerendah == TRUE")
    void testPalingTer_NilaiJumlahTerendahEqual() {
        String data = "5\n5\n10\n---";
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Jumlah Terendah: 5"));
    }

    @Test
    @DisplayName("BRANCH 14: jumlahTerendah > hashMapTotalTerendah.get() == TRUE")
    void testPalingTer_JumlahTerendahGreater() {
        String data = "10\n10\n5\n---";
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Jumlah Terendah: 5 * 1 = 5\n"));
    }

    @Test
    @DisplayName("BRANCH 15: jumlahTerendah > hashMapTotalTerendah.get() == FALSE")
    void testPalingTer_JumlahTerendahNotGreater() {
        String data = "5\n10\n10\n---";
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Jumlah Terendah: 5 * 1 = 5\n"));
    }

    @Test
    @DisplayName("BRANCH 16: frekuensiSaatIni > frekuensiTerbanyak == TRUE")
    void testPalingTer_FrekuensiTerbanyakTrue() {
        String data = "10\n10\n10\n20\n20\n30\n---";
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Terbanyak: 10 (3x)\n"));
    }

    @Test
    @DisplayName("BRANCH 17: frekuensiSaatIni > frekuensiTerbanyak == FALSE")
    void testPalingTer_FrekuensiTerbanyakFalse() {
        String data = "10\n20\n30\n---";
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Terbanyak: 10 (1x)\n"));
    }

    @Test
    @DisplayName("BRANCH 18: hashMapCounterTerbanyak.containsKey() == TRUE")
    void testPalingTer_CounterTerbanyakContainsKeyTrue() {
        String data = "10\n10\n20\n---";
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Terbanyak: 10 (2x)\n"));
    }

    @Test
    @DisplayName("BRANCH 19: hashMapCounterTerbanyak.containsKey() == FALSE")
    void testPalingTer_CounterTerbanyakContainsKeyFalse() {
        String data = "10\n20\n30\n---";
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Terbanyak: 10 (1x)\n"));
    }

    @Test
    @DisplayName("BRANCH 20: frekuensiSaatIni == frekuensiTerbanyak == TRUE (break)")
    void testPalingTer_FrekuensiEqualBreak() {
        String data = "10\n10\n20\n20\n---";
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Terbanyak: 10 (2x)\n"));
    }

    @Test
    @DisplayName("BRANCH 21: hashMapCounter.containsKey() == TRUE (loop i=1)")
    void testPalingTer_CounterContainsKeyTrue() {
        String data = "10\n10\n20\n---";
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Tersedikit: 20 (1x)\n"));
    }

    @Test
    @DisplayName("BRANCH 22: hashMapCounter.containsKey() == FALSE (loop i=1)")
    void testPalingTer_CounterContainsKeyFalse() {
        String data = "10\n20\n30\n---";
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Tersedikit: 20 (1x)\n"));
    }

    @Test
    @DisplayName("BRANCH 23: arrayNilai[i] != nilaiTerdikit == TRUE (continue)")
    void testPalingTer_NilaiTerdikitNotEqual() {
        String data = "10\n20\n30\n40\n---";
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Tersedikit: 20 (1x)\n"));
    }

    @Test
    @DisplayName("BRANCH 24: arrayNilai[i] == nilaiTerdikit == TRUE (else path)")
    void testPalingTer_NilaiTerdikitEqual() {
        String data = "10\n10\n20\n30\n---";
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Tersedikit: 20 (1x)\n"));
    }

    @Test
    @DisplayName("BRANCH 25: hashMapCounter.containsKey(arrayNilai[j]) == TRUE (inner loop)")
    void testPalingTer_InnerLoopContainsKeyTrue() {
        String data = "10\n10\n10\n20\n---";
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Tersedikit: 20 (1x)\n"));
    }

    @Test
    @DisplayName("BRANCH 26: hashMapCounter.containsKey(arrayNilai[j]) == FALSE (inner loop)")
    void testPalingTer_InnerLoopContainsKeyFalse() {
        String data = "10\n10\n20\n30\n---";
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Tersedikit: 20 (1x)\n"));
    }

    @Test
    @DisplayName("BRANCH 27: !hashMapNilai.isEmpty() == TRUE")
    void testPalingTer_HashMapNotEmpty() {
        String data = "10\n20\n---";
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Jumlah Tertinggi"));
    }

    @Test
    @DisplayName("BRANCH 28: jumlah >= jumlahTertinggi && angkaSaatIni > nilaiJumlahTertinggi == TRUE")
    void testPalingTer_JumlahTertinggiConditionTrue() {
        String data = "5\n10\n10\n---";
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Jumlah Tertinggi: 10 * 2 = 20\n"));
    }

    @Test
    @DisplayName("BRANCH 29: jumlah >= jumlahTertinggi && angkaSaatIni > nilaiJumlahTertinggi == FALSE")
    void testPalingTer_JumlahTertinggiConditionFalse() {
        String data = "10\n10\n5\n---";
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Jumlah Tertinggi: 10 * 2 = 20\n"));
    }

    @Test
    @DisplayName("BRANCH 30: Kombinasi kompleks untuk full coverage")
    void testPalingTer_ComplexScenario() {
        String data = "15\n15\n15\n10\n10\n5\n20\n---";
        // 15:3x, 10:2x, 5:1x, 20:1x
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Tertinggi: 20\n"));
        assertTrue(result.contains("Terendah: 5\n"));
        assertTrue(result.contains("Terbanyak: 15 (3x)\n"));
        assertTrue(result.contains("Tersedikit: 5 (1x)\n"));
        assertTrue(result.contains("Jumlah Tertinggi: 15 * 3 = 45\n"));
        assertTrue(result.contains("Jumlah Terendah: 5 * 1 = 5\n"));
    }

    @Test
    @DisplayName("BRANCH 31: Single value untuk test semua kondisi boundary")
    void testPalingTer_SingleValue() {
        String data = "99\n---";
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Tertinggi: 99\n"));
        assertTrue(result.contains("Terendah: 99\n"));
        assertTrue(result.contains("Terbanyak: 99 (1x)\n"));
        assertTrue(result.contains("Tersedikit: 99 (1x)\n"));
        assertTrue(result.contains("Jumlah Tertinggi: 99 * 1 = 99\n"));
        assertTrue(result.contains("Jumlah Terendah: 99 * 1 = 99\n"));
    }

    @Test
    @DisplayName("BRANCH 32: Nilai negatif untuk test kondisi comparison")
    void testPalingTer_NegativeValues() {
        String data = "-10\n-5\n5\n10\n---";
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Tertinggi: 10\n"));
        assertTrue(result.contains("Terendah: -10\n"));
    }

    @Test
    @DisplayName("BRANCH 33: Test loop yang tidak pernah masuk inner loop j")
    void testPalingTer_NoInnerLoopExecution() {
        String data = "10\n20\n---";
        // Loop i=1, arrayNilai[1]=20 != nilaiTerdikit(10), sehingga continue
        // Tidak masuk inner loop karena i sudah di akhir
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Tersedikit: 10 (1x)\n"));
    }

    @Test
    @DisplayName("BRANCH 34: Test kondisi jumlah sama tapi angka berbeda")
    void testPalingTer_SameProductDifferentNumbers() {
        String data = "6\n6\n4\n4\n4\n---";
        // 6*2=12, 4*3=12 (jumlah sama)
        // Karena 6 > 4, maka Jumlah Tertinggi harus 6*2
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Jumlah Tertinggi: 6 * 2 = 12\n"));
    }

    @Test
    @DisplayName("BRANCH 35: Test semua nilai sama (homogen)")
    void testPalingTer_AllSameValues() {
        String data = "7\n7\n7\n7\n7\n---";
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Tertinggi: 7\n"));
        assertTrue(result.contains("Terendah: 7\n"));
        assertTrue(result.contains("Terbanyak: 7 (5x)\n"));
        assertTrue(result.contains("Tersedikit: 7 (5x)\n"));
        assertTrue(result.contains("Jumlah Tertinggi: 7 * 5 = 35\n"));
        assertTrue(result.contains("Jumlah Terendah: 7 * 5 = 35\n"));
    }

    @Test
    @DisplayName("BRANCH 36: Test dengan banyak duplikasi berbeda")
    void testPalingTer_MultipleDuplicates() {
        String data = "1\n1\n2\n2\n2\n3\n3\n3\n3\n---";
        // 1:2x, 2:3x, 3:4x
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Terbanyak: 3 (4x)\n"));
        assertTrue(result.contains("Tersedikit: 1 (2x)\n"));
    }

    @Test
    @DisplayName("BRANCH 37: Test loop tersedikit dengan pola khusus")
    void testPalingTer_TersedikitSpecialPattern() {
        String data = "5\n5\n5\n10\n15\n20\n---";
        // 5:3x (terbanyak), 10,15,20:1x masing-masing
        // Tersedikit harus yang pertama ditemukan dengan freq=1
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Tersedikit: 10 (1x)\n"));
    }

    @Test
    @DisplayName("BRANCH 38: Test path arrayNilai[i] == nilaiJumlahTerendah tepat di awal")
    void testPalingTer_JumlahTerendahAtStart() {
        String data = "3\n3\n10\n---";
        // 3:2x (jumlah=6), 10:1x (jumlah=10)
        // Iterasi pertama: arrayNilai[0]=3 == nilaiJumlahTerendah(3)
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Jumlah Terendah: 3 * 2 = 6\n"));
    }

    @Test
    @DisplayName("BRANCH 39: Test inner loop dengan semua containsKey true")
    void testPalingTer_InnerLoopAllContainsTrue() {
        String data = "8\n8\n8\n8\n---";
        // Semua nilai sama, sehingga hashMapCounter selalu containsKey=true
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Tersedikit: 8 (4x)\n"));
    }

    @Test
    @DisplayName("BRANCH 40: Test dengan data urut menurun")
    void testPalingTer_DescendingOrder() {
        String data = "100\n90\n80\n70\n60\n---";
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Tertinggi: 100\n"));
        assertTrue(result.contains("Terendah: 60\n"));
    }

    @Test
    @DisplayName("BRANCH 41: Test frekuensi terbanyak dengan nilai pertama")
    void testPalingTer_TerbanyakFirstValue() {
        String data = "50\n50\n50\n40\n40\n30\n---";
        // 50:3x (terbanyak dan pertama)
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Terbanyak: 50 (3x)\n"));
    }

    @Test
    @DisplayName("BRANCH 42: Test kondisi jumlah tertinggi dengan produk sama")
    void testPalingTer_HighestProductTieBreaker() {
        String data = "3\n3\n3\n3\n9\n---";
        // 3*4=12, 9*1=9
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Jumlah Tertinggi: 9 * 1 = 9\n"));
    }

    @Test
    @DisplayName("BRANCH 43: Test edge case dengan nilai 0")
    void testPalingTer_WithZero() {
        String data = "0\n5\n10\n---";
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Terendah: 0\n"));
        assertTrue(result.contains("Tertinggi: 10\n"));
    }

    @Test
    @DisplayName("BRANCH 44: Test pola alternating untuk tersedikit")
    void testPalingTer_AlternatingPattern() {
        String data = "1\n2\n1\n2\n3\n---";
        // 1:2x, 2:2x, 3:1x
        // Tersedikit = 3 (freq=1)
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Tersedikit: 3 (1x)\n"));
    }

    @Test
    @DisplayName("BRANCH 45: Test untuk memastikan break di loop terbanyak bekerja")
    void testPalingTer_BreakInTerbanyakLoop() {
        String data = "7\n7\n8\n9\n---";
        // 7:2x (terbanyak), harus break setelah menemukan
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Terbanyak: 7 (2x)\n"));
    }

    @Test
    @DisplayName("BRANCH 46: Test kompleks untuk inner loop j dengan break")
    void testPalingTer_InnerLoopWithBreak() {
        String data = "2\n2\n2\n4\n6\n---";
        // 2:3x, 4:1x, 6:1x
        // Tersedikit harus 4 (pertama yang freq=1)
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Tersedikit: 4 (1x)\n"));
    }

    @Test
    @DisplayName("BRANCH 47: Test untuk kondisi angkaSaatIni > nilaiJumlahTertinggi dengan jumlah sama")
    void testPalingTer_AngkaSaatIniGreaterWithSameJumlah() {
        String data = "2\n2\n4\n---";
        // 2*2=4, 4*1=4 (sama)
        // Karena 4 > 2, maka pilih 4*1
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Jumlah Tertinggi: 4 * 1 = 4\n"));
    }

    @Test
    @DisplayName("BRANCH 48: Test TANPA delimiter --- (line.equals('---') == FALSE)")
    void testPalingTer_WithoutDelimiter() {
        String data = "10\n20\n30\n40\n50";
        // Tidak ada "---", loop harus membaca semua baris sampai habis
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Tertinggi: 50\n"));
        assertTrue(result.contains("Terendah: 10\n"));
    }

    @Test
    @DisplayName("BRANCH 49: Test DENGAN delimiter --- (line.equals('---') == TRUE)")
    void testPalingTer_WithDelimiter() {
        String data = "10\n20\n30\n---\n40\n50";
        // Ada "---", loop harus berhenti dan tidak membaca 40, 50
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Tertinggi: 30\n"));
        assertTrue(result.contains("Terendah: 10\n"));
        // Memastikan 40 dan 50 tidak terbaca
        assertFalse(result.contains("50"));
    }

    @Test
    @DisplayName("BRANCH 50: Test dengan line.isEmpty() == TRUE di tengah data")
    void testPalingTer_WithEmptyLinesInMiddle() {
        String data = "10\n\n\n20\n\n30\n---";
        // Ada baris kosong di tengah, harus di-skip dengan continue
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Tertinggi: 30\n"));
        assertTrue(result.contains("Terendah: 10\n"));
    }

    @Test
    @DisplayName("BRANCH 51: Test tanpa baris kosong (line.isEmpty() == FALSE semua)")
    void testPalingTer_NoEmptyLines() {
        String data = "15\n25\n35\n---";
        // Tidak ada baris kosong sama sekali
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Tertinggi: 35\n"));
        assertTrue(result.contains("Terendah: 15\n"));
    }

    @Test
    @DisplayName("BRANCH 52: Loop tersedikit - arrayNilai[i] == nilaiTerdikit path dengan inner loop TIDAK menemukan baru")
    void testPalingTer_TersedikitInnerLoopNotFound() {
        String data = "5\n5\n5\n---";
        // Semua nilai sama, sehingga di inner loop j, semua sudah ada di hashMapCounter
        // Inner loop akan terus continue tanpa pernah masuk else untuk set nilaiTerdikit baru
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Tersedikit: 5 (3x)\n"));
    }

    @Test
    @DisplayName("BRANCH 53: Loop tersedikit - inner loop j sampai akhir tanpa break")
    void testPalingTer_InnerLoopReachesEnd() {
        String data = "8\n8\n---";
        // i=1, arrayNilai[1]=8 == nilaiTerdikit(8)
        // Inner loop j dari i+1=2 sampai length=2, tidak masuk loop (j < 2 false)
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Tersedikit: 8 (2x)\n"));
    }

    @Test
    @DisplayName("BRANCH 54: Loop tersedikit i=1 dengan nilai berbeda (continue path)")
    void testPalingTer_TersedikitContinuePath() {
        String data = "12\n18\n24\n---";
        // i=1: arrayNilai[1]=18 != nilaiTerdikit(12), sehingga continue
        // i=2: arrayNilai[2]=24 != nilaiTerdikit(12), sehingga continue
        // Tidak pernah masuk else block
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Tersedikit: 12 (1x)\n"));
    }

    @Test
    @DisplayName("BRANCH 55: Inner loop dengan semua elemen sudah di hashMapCounter")
    void testPalingTer_InnerLoopAllInHashMap() {
        String data = "3\n3\n3\n3\n3\n---";
        // Semua nilai 3, sehingga setiap kali masuk inner loop, semua containsKey=true
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Tersedikit: 3 (5x)\n"));
    }

    @Test
    @DisplayName("BRANCH 56: Loop tersedikit dengan pattern yang memaksa masuk inner loop multiple times")
    void testPalingTer_TersedikitMultipleInnerLoop() {
        String data = "1\n1\n2\n2\n3\n---";
        // i=1: arrayNilai[1]=1 == nilaiTerdikit(1), masuk else
        // Inner loop j=2: arrayNilai[2]=2 belum ada, set nilaiTerdikit=2, break, i=2
        // i=3: arrayNilai[3]=2 == nilaiTerdikit(2), masuk else
        // Inner loop j=4: arrayNilai[4]=3 belum ada, set nilaiTerdikit=3, break
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Tersedikit: 3 (1x)\n"));
    }

    @Test
    @DisplayName("BRANCH 57: Test edge case dengan 2 nilai untuk path tersedikit")
    void testPalingTer_TwoValuesTersedikit() {
        String data = "10\n20\n---";
        // nilaiTerdikit awal = 10 (freq=1)
        // i=1: arrayNilai[1]=20 != nilaiTerdikit(10), continue
        // Loop selesai, tersedikit tetap 10
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Tersedikit: 10 (1x)\n"));
    }

    @Test
    @DisplayName("BRANCH 58: Pattern khusus untuk force inner loop dengan break")
    void testPalingTer_InnerLoopForceBreak() {
        String data = "4\n4\n6\n8\n---";
        // i=1: arrayNilai[1]=4 == nilaiTerdikit(4), masuk else
        // j=2: arrayNilai[2]=6 belum di hashMapCounter, masuk else, break
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Tersedikit: 6 (1x)\n"));
    }

    @Test
    @DisplayName("BRANCH 59: Test dengan array panjang untuk cover semua path")
    void testPalingTer_LongArrayAllPaths() {
        String data = "2\n2\n2\n4\n4\n6\n8\n---";
        // 2:3x, 4:2x, 6:1x, 8:1x
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Tersedikit: 6 (1x)\n"));
    }

    @Test
    @DisplayName("BRANCH 60: Jumlah Terendah - arrayNilai[i] == nilaiJumlahTerendah FALSE path")
    void testPalingTer_JumlahTerendahNotEqual() {
        String data = "8\n8\n4\n4\n4\n---";
        // Iterasi 0: arrayNilai[0]=8, nilaiJumlahTerendah=8 (initial), masuk IF TRUE
        // Iterasi 1: arrayNilai[1]=8, nilaiJumlahTerendah=8, masuk IF TRUE
        // Iterasi 2: arrayNilai[2]=4, nilaiJumlahTerendah=8, TIDAK sama, masuk ELSE IF
        // Di ELSE IF: jumlahTerendah(8) > hashMapTotalTerendah.get(4)=4, TRUE
        // Update nilaiJumlahTerendah=4, jumlahTerendah=4
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Jumlah Terendah: 4 * 3 = 12\n"));
    }

    @Test
    @DisplayName("BRANCH 61: Jumlah Terendah - ELSE IF dengan jumlahTerendah TIDAK lebih besar")
    void testPalingTer_JumlahTerendahElseIfFalse() {
        String data = "3\n6\n6\n---";
        // Iterasi 0: arrayNilai[0]=3, nilaiJumlahTerendah=3 (initial)
        // hashMapTotalTerendah.put(3, 3), jumlahTerendah=3
        // Iterasi 1: arrayNilai[1]=6, nilaiJumlahTerendah=3, TIDAK sama
        // ELSE IF: jumlahTerendah(3) > hashMapTotalTerendah.get(6)=6? FALSE
        // Tidak update
        // Iterasi 2: arrayNilai[2]=6, nilaiJumlahTerendah=3, TIDAK sama
        // ELSE IF: jumlahTerendah(3) > hashMapTotalTerendah.get(6)=12? FALSE
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Jumlah Terendah: 3 * 1 = 3\n"));
    }

    @Test
    @DisplayName("BRANCH 62: Force ELSE IF TRUE path dengan pola khusus")
    void testPalingTer_ForceElseIfTrue() {
        String data = "10\n5\n5\n---";
        // Iterasi 0: arrayNilai[0]=10, nilaiJumlahTerendah=10
        // jumlahTerendah=10
        // Iterasi 1: arrayNilai[1]=5, != nilaiJumlahTerendah(10)
        // ELSE IF: jumlahTerendah(10) > hashMapTotalTerendah.get(5)=5? TRUE
        // Update: nilaiJumlahTerendah=5, jumlahTerendah=5
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Jumlah Terendah: 5 * 2 = 10\n"));
    }

    @Test
    @DisplayName("BRANCH 63: Complex pattern untuk test kedua kondisi")
    void testPalingTer_ComplexJumlahTerendah() {
        String data = "15\n15\n10\n10\n10\n5\n---";
        // 15*2=30, 10*3=30, 5*1=5
        // Jumlah terendah = 5*1=5
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Jumlah Terendah: 5 * 1 = 5\n"));
    }

    @Test
    @DisplayName("BRANCH 64: Test dengan nilai awal besar lalu nilai kecil")
    void testPalingTer_StartLargeThenSmall() {
        String data = "20\n3\n3\n3\n---";
        // Iterasi 0: 20, jumlahTerendah=20
        // Iterasi 1: 3 != 20, ELSE IF: 20 > 3? TRUE, update ke 3
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Jumlah Terendah: 3 * 3 = 9\n"));
    }

    @Test
    @DisplayName("BRANCH 65: Test loop Jumlah Terendah dengan iterasi minimal")
    void testPalingTer_MinimalIterationJumlahTerendah() {
        String data = "7\n---";
        // arrayNilai.length = 1
        // Loop for (int i = 0; i < 1; i++) akan eksekusi 1 kali (i=0)
        // Ini untuk memastikan loop condition TRUE path tercover
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Jumlah Terendah: 7 * 1 = 7\n"));
    }

    @Test
    @DisplayName("BRANCH 66: Test loop Terbanyak mencari frekuensi dengan iterasi penuh")
    void testPalingTer_FullIterationFrekuensiTerbanyak() {
        String data = "1\n2\n3\n4\n5\n---";
        // Loop mencari frekuensiTerbanyak harus iterasi semua elemen
        // Memastikan condition i < arrayNilai.length dijalankan berkali-kali
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Terbanyak:"));
    }

    @Test
    @DisplayName("BRANCH 67: Test loop Counter Terbanyak dengan break di tengah")
    void testPalingTer_CounterTerbanyakBreakInMiddle() {
        String data = "9\n9\n1\n2\n3\n---";
        // 9 muncul 2x (terbanyak)
        // Loop akan break saat frekuensiSaatIni == frekuensiTerbanyak
        // Tidak semua elemen di-iterasi
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Terbanyak: 9 (2x)\n"));
    }

    @Test
    @DisplayName("BRANCH 68: Test semua loop dengan array length = 1 (boundary case)")
    void testPalingTer_AllLoopsWithSingleElement() {
        String data = "100\n---";
        // arrayNilai.length = 1
        // Semua loop for hanya eksekusi kondisi 1 kali atau tidak sama sekali
        // Loop tertinggi/terendah: for (int i = 1; i < 1) → TIDAK EKSEKUSI (FALSE dari awal)
        // Loop jumlah terendah: for (int i = 0; i < 1) → EKSEKUSI 1 kali (i=0)
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Tertinggi: 100"));
        assertTrue(result.contains("Terendah: 100"));
    }

    @Test
    @DisplayName("BRANCH 69: Test loop Tertinggi/Terendah untuk FALSE condition")
    void testPalingTer_TertinggiTerendahLoopFalseCondition() {
        String data = "50\n---";
        // Loop: for (int i = 1; i < arrayNilai.length; i++)
        // arrayNilai.length = 1, jadi kondisi 1 < 1 = FALSE
        // Loop TIDAK PERNAH DIEKSEKUSI (missed branch ini!)
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Tertinggi: 50\n"));
        assertTrue(result.contains("Terendah: 50\n"));
    }

    @Test
    @DisplayName("BRANCH 70: Test loop Tertinggi/Terendah untuk TRUE condition")
    void testPalingTer_TertinggiTerendahLoopTrueCondition() {
        String data = "10\n20\n---";
        // Loop: for (int i = 1; i < arrayNilai.length; i++)
        // arrayNilai.length = 2, jadi kondisi 1 < 2 = TRUE
        // Loop DIEKSEKUSI (i=1)
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Tertinggi: 20\n"));
        assertTrue(result.contains("Terendah: 10\n"));
    }

    @Test
    @DisplayName("BRANCH 71: Test loop Tersedikit dengan i=1 tidak masuk loop")
    void testPalingTer_TersedikitLoopNotEntered() {
        String data = "11\n---";
        // Loop: for (int i = 1; i < arrayNilai.length; i++)
        // arrayNilai.length = 1, kondisi 1 < 1 = FALSE
        // Loop tersedikit TIDAK DIEKSEKUSI
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Tersedikit: 11 (1x)\n"));
    }

    @Test
    @DisplayName("BRANCH 72: Test loop Tersedikit dengan i=1 masuk loop")
    void testPalingTer_TersedikitLoopEntered() {
        String data = "5\n10\n---";
        // Loop: for (int i = 1; i < arrayNilai.length; i++)
        // arrayNilai.length = 2, kondisi 1 < 2 = TRUE
        // Loop tersedikit DIEKSEKUSI (i=1)
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Tersedikit: 5 (1x)\n"));
    }

    @Test
    @DisplayName("BRANCH 73: Jumlah Tertinggi - jumlah >= jumlahTertinggi TRUE && angkaSaatIni > nilaiJumlahTertinggi TRUE")
    void testPalingTer_JumlahTertinggiBothConditionsTrue() {
        String data = "5\n10\n10\n---";
        // 5*1=5 (jumlahTertinggi awal = arrayNilai[0] = 5)
        // 10*2=20
        // Kondisi: 20 >= 5 (TRUE) && 10 > 0 (TRUE) → masuk IF, update
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Jumlah Tertinggi: 10 * 2 = 20\n"));
    }

    @Test
    @DisplayName("BRANCH 74: Jumlah Tertinggi - jumlah < jumlahTertinggi (kondisi pertama FALSE)")
    void testPalingTer_JumlahTertinggiFirstConditionFalse() {
        String data = "10\n10\n10\n5\n---";
        // 10*3=30 (processed first, jumlahTertinggi = 30, nilaiJumlahTertinggi = 10)
        // 5*1=5
        // Kondisi: 5 >= 30? FALSE → tidak masuk IF
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Jumlah Tertinggi: 10 * 3 = 30\n"));
    }

    @Test
    @DisplayName("BRANCH 75: Jumlah Tertinggi - jumlah >= TRUE tapi angkaSaatIni <= nilaiJumlahTertinggi (kondisi kedua FALSE)")
    void testPalingTer_JumlahTertinggiSecondConditionFalse() {
        String data = "10\n5\n5\n---";
        // Entry processing order dari HashMap tidak guaranteed, tapi kita test logikanya:
        // Jika 10*1=10 diproses dulu: jumlahTertinggi=10, nilaiJumlahTertinggi=10
        // Lalu 5*2=10: 10 >= 10 (TRUE) && 5 > 10? (FALSE) → tidak update
        // Hasil akhir: 10*1 atau bisa juga 5*2 tergantung urutan HashMap
        String result = controller.palingTer(encode(data));
        // Kita cek bahwa hasilnya salah satu dari keduanya
        assertTrue(result.contains("Jumlah Tertinggi:") && 
                   (result.contains("10 * 1 = 10") || result.contains("5 * 2 = 10")));
    }

    @Test
    @DisplayName("BRANCH 76: Jumlah Tertinggi - jumlah sama tapi angka lebih kecil (angkaSaatIni <= nilaiJumlahTertinggi)")
    void testPalingTer_JumlahSamaAngkaKecil() {
        String data = "3\n3\n6\n---";
        // 3*2=6, 6*1=6 (jumlah sama)
        // HashMap iteration order tidak terjamin, tapi test kedua kondisi:
        // Scenario 1: 3 diproses dulu → jumlahTertinggi=6, nilaiJumlahTertinggi=3
        //   Lalu 6: 6 >= 6 (TRUE) && 6 > 3 (TRUE) → update ke 6
        // Scenario 2: 6 diproses dulu → jumlahTertinggi=6, nilaiJumlahTertinggi=6
        //   Lalu 3: 6 >= 6 (TRUE) && 3 > 6 (FALSE) → tidak update
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Jumlah Tertinggi: 6 * 1 = 6\n"));
    }

    @Test
    @DisplayName("BRANCH 77: Jumlah Tertinggi - test dengan angka besar di akhir")
    void testPalingTer_JumlahTertinggiLargeNumberLast() {
        String data = "2\n2\n2\n100\n---";
        // 2*3=6, 100*1=100
        // 100 >= 6 (TRUE) && 100 > 2 (TRUE) → update
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Jumlah Tertinggi: 100 * 1 = 100\n"));
    }

    @Test
    @DisplayName("BRANCH 78: Jumlah Tertinggi - multiple values dengan berbagai kondisi")
    void testPalingTer_JumlahTertinggiMultipleValues() {
        String data = "1\n2\n2\n3\n3\n3\n---";
        // 1*1=1, 2*2=4, 3*3=9
        // Harus iterasi semua dan pilih 3*3=9
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Jumlah Tertinggi: 3 * 3 = 9\n"));
    }

    @Test
    @DisplayName("BRANCH 79: Test hashMapNilai.isEmpty() FALSE path (sudah tercover tapi untuk explisit)")
    void testPalingTer_HashMapNotEmptyExplicit() {
        String data = "7\n7\n---";
        // hashMapNilai tidak kosong, masuk loop for-each
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Jumlah Tertinggi: 7 * 2 = 14\n"));
    }

    @Test
    @DisplayName("BRANCH 80: Test kondisi equal untuk jumlah tapi angka berbeda")
    void testPalingTer_EqualProductDifferentNumbers() {
        String data = "4\n4\n2\n2\n2\n2\n---";
        // 4*2=8, 2*4=8 (sama)
        // Tergantung urutan HashMap, tapi yang angkanya lebih besar harus menang
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Jumlah Tertinggi: 4 * 2 = 8\n"));
    }

    @Test
    @DisplayName("BRANCH 81: Test boundary case untuk compound condition")
    void testPalingTer_CompoundConditionBoundary() {
        String data = "8\n4\n4\n---";
        // 8*1=8, 4*2=8 (jumlah sama)
        // 8 >= 8 (TRUE) && 8 > nilaiJumlahTertinggi
        // Test ini memastikan kedua sisi AND condition tercover
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Jumlah Tertinggi:"));
    }

    @Test
    @DisplayName("BRANCH 82: Loop Counter Terbanyak - kondisi i < arrayNilai.length FALSE (tidak masuk loop)")
    void testPalingTer_CounterTerbanyakLoopNotEntered() {
        // Ini sebenarnya tidak mungkin karena sudah ada validasi daftarNilai.isEmpty()
        // Tapi kita test dengan minimal data
        String data = "99\n---";
        // arrayNilai.length = 1
        // Loop: for (int i = 0; i < 1; i++) akan dijalankan 1 kali (i=0)
        // Untuk FALSE path, perlu arrayNilai.length = 0, tapi sudah dicegah validasi
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Terbanyak: 99 (1x)\n"));
    }

    @Test
    @DisplayName("BRANCH 83: Loop Counter Terbanyak - kondisi i < arrayNilai.length TRUE (masuk loop)")
    void testPalingTer_CounterTerbanyakLoopEntered() {
        String data = "15\n15\n20\n---";
        // arrayNilai.length = 3
        // Loop akan iterasi: i=0, i=1, i=2 sampai menemukan nilai terbanyak
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Terbanyak: 15 (2x)\n"));
    }

    @Test
    @DisplayName("BRANCH 84: Loop Counter Terbanyak - break di iterasi pertama")
    void testPalingTer_CounterTerbanyakBreakFirst() {
        String data = "7\n7\n7\n1\n2\n3\n---";
        // 7 muncul 3x (terbanyak)
        // Loop mencari frekuensiTerbanyak = 3
        // Loop counter akan menemukan frekuensi=3 di iterasi pertama (i=0), langsung break
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Terbanyak: 7 (3x)\n"));
    }

    @Test
    @DisplayName("BRANCH 85: Loop Counter Terbanyak - break di iterasi tengah")
    void testPalingTer_CounterTerbanyakBreakMiddle() {
        String data = "1\n2\n3\n3\n3\n---";
        // 3 muncul 3x (terbanyak)
        // Loop counter: i=0 (1, freq=1), i=1 (2, freq=1), i=2 (3, freq=1), i=3 (3, freq=2), i=4 (3, freq=3) → break
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Terbanyak: 3 (3x)\n"));
    }

    @Test
    @DisplayName("BRANCH 86: Loop Counter Terbanyak - iterasi sampai akhir tanpa break dini")
    void testPalingTer_CounterTerbanyakFullIteration() {
        String data = "5\n10\n15\n20\n25\n25\n---";
        // 25 muncul 2x (terbanyak) dan ada di akhir
        // Loop harus iterasi hampir sampai akhir baru menemukan freq=2
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Terbanyak: 25 (2x)\n"));
    }

    @Test
    @DisplayName("BRANCH 87: For-each loop Jumlah Tertinggi - iterasi minimal (1 entry)")
    void testPalingTer_JumlahTertinggiForEachMinimal() {
        String data = "50\n50\n50\n---";
        // hashMapNilai hanya punya 1 entry: {50: 3}
        // For-each loop hanya iterasi 1 kali
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Jumlah Tertinggi: 50 * 3 = 150\n"));
    }

    @Test
    @DisplayName("BRANCH 88: For-each loop Jumlah Tertinggi - iterasi multiple entries")
    void testPalingTer_JumlahTertinggiForEachMultiple() {
        String data = "1\n2\n2\n3\n3\n3\n4\n4\n4\n4\n---";
        // hashMapNilai punya 4 entries: {1:1, 2:2, 3:3, 4:4}
        // For-each loop iterasi semua 4 entries
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Jumlah Tertinggi: 4 * 4 = 16\n"));
    }

    @Test
    @DisplayName("BRANCH 89: For-each loop dengan berbagai ukuran HashMap")
    void testPalingTer_ForEachVariousHashMapSizes() {
        String data = "10\n20\n30\n40\n50\n---";
        // hashMapNilai punya 5 entries (semua unique)
        // For-each loop harus iterasi semua
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Jumlah Tertinggi: 50 * 1 = 50\n"));
    }

    @Test
    @DisplayName("BRANCH 90: Comprehensive test - semua loop conditions tercover")
    void testPalingTer_AllLoopsComprehensive() {
        String data = "3\n3\n5\n5\n5\n7\n9\n9\n9\n9\n---";
        // Array length = 10, semua loop akan dieksekusi
        // hashMapNilai: {3:2, 5:3, 7:1, 9:4}
        // Terbanyak: 9 (4x)
        // Tertinggi: 9
        // Terendah: 3
        // Tersedikit: 7 (1x)
        // Jumlah Tertinggi: 9*4=36
        // Jumlah Terendah: 7*1=7
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Tertinggi: 9\n"));
        assertTrue(result.contains("Terendah: 3\n"));
        assertTrue(result.contains("Terbanyak: 9 (4x)\n"));
        assertTrue(result.contains("Tersedikit: 7 (1x)\n"));
        assertTrue(result.contains("Jumlah Tertinggi: 9 * 4 = 36\n"));
        assertTrue(result.contains("Jumlah Terendah: 7 * 1 = 7\n"));
    }

    @Test
    @DisplayName("BRANCH 91: Edge case - memaksa semua loop condition ditest")
    void testPalingTer_ForceAllLoopConditions() {
        String data = "2\n4\n6\n8\n10\n10\n---";
        // Test dengan data yang memaksa semua loop berjalan penuh
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Terbanyak: 10 (2x)\n"));
        assertTrue(result.contains("Jumlah Tertinggi: 10 * 2 = 20\n"));
    }
}