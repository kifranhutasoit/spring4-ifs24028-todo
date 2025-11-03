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
    @DisplayName("Input kosong")
    void testPalingTer_EmptyInput() {
        String data = "";
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Data nilai kosong"));
    }

    @Test
    @DisplayName("DaftarNilai kosong setelah parsing")
    void testPalingTer_NoValidData() {
        String data = "\n\n\n---";
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Tidak ada nilai yang dimasukkan"));
    }

    @Test
    @DisplayName("Input Base64 tidak valid")
    void testPalingTer_InvalidBase64() {
        String result = controller.palingTer("%%%invalid%%%");
        assertTrue(result.contains("Error processing data"));
    }

    @Test
    @DisplayName("NumberFormatException saat parsing")
    void testPalingTer_CatchNumberFormatException() {
        String data = "10\n20\ntiga puluh\n---";
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Error processing data"));
    }

    @Test
    @DisplayName("HashMap containsKey TRUE")
    void testPalingTer_ContainsKeyTrue() {
        String data = "10\n10\n10\n---";
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Terbanyak: 10 (3x)\n"));
    }

    @Test
    @DisplayName("HashMap containsKey FALSE")
    void testPalingTer_ContainsKeyFalse() {
        String data = "10\n20\n30\n---";
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Tersedikit: 10 (1x)\n"));
    }

    @Test
    @DisplayName("Nilai tertinggi TRUE")
    void testPalingTer_TertinggiTrue() {
        String data = "5\n10\n15\n20\n---";
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Tertinggi: 20\n"));
    }

    @Test
    @DisplayName("Nilai tertinggi FALSE")
    void testPalingTer_TertinggiFalse() {
        String data = "20\n15\n10\n5\n---";
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Tertinggi: 20\n"));
    }

    @Test
    @DisplayName("Nilai terendah TRUE")
    void testPalingTer_TerendahTrue() {
        String data = "20\n15\n10\n5\n---";
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Terendah: 5\n"));
    }

    @Test
    @DisplayName("Nilai terendah FALSE")
    void testPalingTer_TerendahFalse() {
        String data = "5\n10\n15\n20\n---";
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Terendah: 5\n"));
    }

    @Test
    @DisplayName("hashMapTotalTerendah containsKey TRUE")
    void testPalingTer_TotalTerendahContainsTrue() {
        String data = "10\n10\n20\n---";
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Jumlah Terendah"));
    }

    @Test
    @DisplayName("hashMapTotalTerendah containsKey FALSE")
    void testPalingTer_TotalTerendahContainsFalse() {
        String data = "10\n20\n30\n---";
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Jumlah Terendah: 10 * 1 = 10\n"));
    }

    @Test
    @DisplayName("arrayNilai[i] == nilaiJumlahTerendah TRUE")
    void testPalingTer_JumlahTerendahEqual() {
        String data = "5\n5\n10\n---";
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Jumlah Terendah"));
    }

    @Test
    @DisplayName("jumlahTerendah > hashMap TRUE")
    void testPalingTer_JumlahTerendahGreater() {
        String data = "10\n10\n5\n---";
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Jumlah Terendah: 5 * 1 = 5\n"));
    }

  
    @Test
    @DisplayName("hashMapCounter containsKey TRUE")
    void testPalingTer_CounterContainsTrue() {
        String data = "10\n10\n20\n---";
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Tersedikit: 20 (1x)\n"));
    }

    @Test
    @DisplayName("arrayNilai[i] != nilaiTerdikit TRUE (continue)")
    void testPalingTer_NilaiTerdikitNotEqual() {
        String data = "10\n20\n30\n---";
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Tersedikit: 10 (1x)\n"));
    }

    @Test
    @DisplayName("arrayNilai[i] == nilaiTerdikit TRUE (else)")
    void testPalingTer_NilaiTerdikitEqual() {
        String data = "10\n10\n20\n30\n---";
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Tersedikit: 20 (1x)\n"));
    }

    @Test
    @DisplayName("Inner loop hashMapCounter containsKey TRUE")
    void testPalingTer_InnerLoopContainsTrue() {
        String data = "10\n10\n10\n20\n---";
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Tersedikit: 20 (1x)\n"));
    }

    @Test
    @DisplayName("Inner loop hashMapCounter containsKey FALSE")
    void testPalingTer_InnerLoopContainsFalse() {
        String data = "10\n10\n20\n30\n---";
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Tersedikit: 20 (1x)\n"));
    }

    @Test
    @DisplayName("For-each loop minimal (1 entry)")
    void testPalingTer_ForEachMinimal() {
        String data = "50\n50\n50\n---";
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Jumlah Tertinggi: 50 * 3 = 150\n"));
    }

    @Test
    @DisplayName("For-each loop multiple entries")
    void testPalingTer_ForEachMultiple() {
        String data = "1\n2\n2\n3\n3\n3\n---";
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Jumlah Tertinggi: 3 * 3 = 9\n"));
    }

    @Test
    @DisplayName("Compound condition: jumlah >= jumlahTertinggi TRUE && angka > nilaiJumlahTertinggi TRUE")
    void testPalingTer_CompoundBothTrue() {
        String data = "5\n10\n10\n---";
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Jumlah Tertinggi: 10 * 2 = 20\n"));
    }

    @Test
    @DisplayName("Compound condition: jumlah < jumlahTertinggi FALSE")
    void testPalingTer_CompoundFirstFalse() {
        String data = "10\n10\n10\n5\n---";
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Jumlah Tertinggi: 10 * 3 = 30\n"));
    }

    @Test
    @DisplayName("Compound condition: angka <= nilaiJumlahTertinggi FALSE")
    void testPalingTer_CompoundSecondFalse() {
        String data = "10\n5\n5\n---";
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Jumlah Tertinggi:") && 
                   (result.contains("10 * 1 = 10") || result.contains("5 * 2 = 10")));
    }

    

   
    @Test
    @DisplayName("Line equals --- TRUE (dengan delimiter)")
    void testPalingTer_WithDelimiter() {
        String data = "10\n20\n30\n---\n40\n50";
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Tertinggi: 30\n"));
    }

    @Test
    @DisplayName("Line equals --- FALSE (tanpa delimiter)")
    void testPalingTer_WithoutDelimiter() {
        String data = "10\n20\n30\n40\n50";
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Tertinggi: 50\n"));
    }

    @Test
    @DisplayName("Line isEmpty TRUE")
    void testPalingTer_WithEmptyLines() {
        String data = "10\n\n20\n\n30\n---";
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Tertinggi: 30\n"));
    }

    @Test
    @DisplayName("Single value test")
    void testPalingTer_SingleValue() {
        String data = "99\n---";
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Tertinggi: 99\n"));
        assertTrue(result.contains("Terendah: 99\n"));
    }

    @Test
    @DisplayName("Loop tertinggi/terendah dengan length=1 (tidak masuk loop)")
    void testPalingTer_LoopNotEntered() {
        String data = "50\n---";
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Tertinggi: 50\n"));
        assertTrue(result.contains("Terendah: 50\n"));
    }

    @Test
    @DisplayName("Loop tertinggi/terendah dengan length>1 (masuk loop)")
    void testPalingTer_LoopEntered() {
        String data = "10\n20\n---";
        String result = controller.palingTer(encode(data));
        assertTrue(result.contains("Tertinggi: 20\n"));
        assertTrue(result.contains("Terendah: 10\n"));
    }
}