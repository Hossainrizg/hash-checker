package com.smlnskgmail.jaman.hashchecker.hashgenerator;

import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.smlnskgmail.jaman.hashchecker.hashgenerator.support.HashType;
import com.smlnskgmail.jaman.hashchecker.support.logger.L;
import com.smlnskgmail.jaman.hashchecker.support.prefs.PrefsHelper;
import com.smlnskgmail.jaman.hashchecker.utils.HashUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.CRC32;

public class HashCalculator {

    private HashType hashType;

    public HashCalculator(@NonNull HashType hashType) {
        this.hashType = hashType;
    }

    @Nullable
    public String generateFromString(@NonNull String text) {
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        if (HashType.isMessageDigestUtilSupport(hashType)) {
            try {
                MessageDigest messageDigest = MessageDigest.getInstance(hashType.getMessageDigestHashName());
                messageDigest.update(bytes);
                return HashUtils.getStringFromBytes(messageDigest.digest());
            } catch (NoSuchAlgorithmException e) {
                L.e(e);
                return null;
            }
        } else {
            CRC32 crc32 = new CRC32();
            crc32.update(bytes);
            return Long.toHexString(crc32.getValue());
        }
    }

    @Nullable
    String generateFromFile(@NonNull Context context, @NonNull Uri path) {
        try {
            InputStream fileStream = getInputStreamFromUri(context, path);
            return generateFromFile(fileStream);
        } catch (Exception e) {
            L.e(e);
            return null;
        }
    }

    private InputStream getInputStreamFromUri(@NonNull Context context, @NonNull Uri path)
            throws Exception {
        if (!PrefsHelper.isUsingInnerFileManager(context)
                || PrefsHelper.getGenerateFromShareIntentStatus(context)) {
            return context.getContentResolver().openInputStream(path);
        }
        return new FileInputStream(new File(new URI(path.toString())));
    }

    @Nullable
    public String generateFromFile(@Nullable InputStream inputStream) throws Exception {
        if (inputStream != null) {
            byte[] buffer = new byte[1024];
            CheckerMessageDigest checkerMessageDigest = new CheckerMessageDigest(hashType);
            checkerMessageDigest.init();
            int read;
            do {
                read = inputStream.read(buffer);
                if (read > 0) {
                    checkerMessageDigest.update(buffer, read);
                }
            } while (read != -1);
            return checkerMessageDigest.getResult();
        }
        return null;
    }

}
