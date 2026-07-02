package com.cybertrace.security;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Utilitaire de chiffrement/déchiffrement symétrique (AES-256-CBC)
 * pour la protection des données sensibles CTI (IOC, identités d'acteurs...).
 *
 * Format de sortie : Base64( IV(16 octets) + texte chiffré )
 * Un IV aléatoire est généré à chaque appel à encrypt(), garantissant
 * que deux chiffrements du même texte produisent des résultats différents.
 */
public final class CryptoUtils {

    private static final String ALGO = "AES/CBC/PKCS5Padding";
    private static final int IV_LENGTH = 16;

    // Clé statique dérivée par hachage — suffisant pour ce projet pédagogique.
    // Dans un contexte réel, la clé serait gérée par un coffre-fort de secrets
    // (Vault, AWS KMS...) et jamais codée en dur dans le source.
    private static final String PASSPHRASE = "CyberTrace-CTI-Platform-2026";

    private CryptoUtils() {
        // classe utilitaire non instanciable
    }

    private static SecretKeySpec buildKey() throws Exception {
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] keyBytes = sha.digest(PASSPHRASE.getBytes(StandardCharsets.UTF_8));
        return new SecretKeySpec(keyBytes, "AES"); // AES-256
    }

    /**
     * Chiffre un texte en clair et retourne une chaîne Base64
     * contenant l'IV suivi du texte chiffré.
     */
    public static String encrypt(String plainText) throws Exception {
        byte[] iv = new byte[IV_LENGTH];
        new SecureRandom().nextBytes(iv);

        Cipher cipher = Cipher.getInstance(ALGO);
        cipher.init(Cipher.ENCRYPT_MODE, buildKey(), new IvParameterSpec(iv));

        byte[] cipherBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

        byte[] combined = new byte[iv.length + cipherBytes.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(cipherBytes, 0, combined, iv.length, cipherBytes.length);

        return Base64.getEncoder().encodeToString(combined);
    }

    /**
     * Déchiffre une chaîne produite par encrypt().
     * Lève une exception si les données sont corrompues ou invalides.
     */
    public static String decrypt(String encoded) throws Exception {
        byte[] combined = Base64.getDecoder().decode(encoded);

        if (combined.length < IV_LENGTH) {
            throw new IllegalArgumentException("Données chiffrées invalides (trop courtes)");
        }

        byte[] iv = new byte[IV_LENGTH];
        byte[] cipherBytes = new byte[combined.length - IV_LENGTH];
        System.arraycopy(combined, 0, iv, 0, IV_LENGTH);
        System.arraycopy(combined, IV_LENGTH, cipherBytes, 0, cipherBytes.length);

        Cipher cipher = Cipher.getInstance(ALGO);
        cipher.init(Cipher.DECRYPT_MODE, buildKey(), new IvParameterSpec(iv));

        byte[] plainBytes = cipher.doFinal(cipherBytes);
        return new String(plainBytes, StandardCharsets.UTF_8);
    }
}
