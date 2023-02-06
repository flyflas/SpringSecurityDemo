package icu.xiaobai.book.entity.response;

import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

/**
 * 自定义Http请求状态
 * Copy from org.springframework.http.HttpStatus
 */
public enum CustomHttpStatus {
    // 4xx client error
    ACCOUNT_SUSPEND(461, HttpStatus.Series.CLIENT_ERROR, "Account Suspend"),
    ACCOUNT_EXCEPTION(460, HttpStatus.Series.CLIENT_ERROR, "Account Exception")
    ;

    private static final CustomHttpStatus[] VALUES;

    static {
        VALUES = values();
    }

    private final int value;

    private final HttpStatus.Series series;

    private final String reasonPhrase;

    CustomHttpStatus(int value, HttpStatus.Series series, String reasonPhrase) {
        this.value = value;
        this.series = series;
        this.reasonPhrase = reasonPhrase;
    }


    /**
     * Return the integer value of this status code.
     */
    public int value() {
        return this.value;
    }

    /**
     * Return the HTTP status series of this status code.
     * @see HttpStatus.Series
     */
    public HttpStatus.Series series() {
        return this.series;
    }

    /**
     * Return the reason phrase of this status code.
     */
    public String getReasonPhrase() {
        return this.reasonPhrase;
    }

    /**
     * Whether this status code is in the HTTP series
     * {@link org.springframework.http.HttpStatus.Series#INFORMATIONAL}.
     * <p>This is a shortcut for checking the value of {@link #series()}.
     * @since 4.0
     * @see #series()
     */
    public boolean is1xxInformational() {
        return (series() == HttpStatus.Series.INFORMATIONAL);
    }

    /**
     * Whether this status code is in the HTTP series
     * {@link org.springframework.http.HttpStatus.Series#SUCCESSFUL}.
     * <p>This is a shortcut for checking the value of {@link #series()}.
     * @since 4.0
     * @see #series()
     */
    public boolean is2xxSuccessful() {
        return (series() == HttpStatus.Series.SUCCESSFUL);
    }

    /**
     * Whether this status code is in the HTTP series
     * {@link org.springframework.http.HttpStatus.Series#REDIRECTION}.
     * <p>This is a shortcut for checking the value of {@link #series()}.
     * @since 4.0
     * @see #series()
     */
    public boolean is3xxRedirection() {
        return (series() == HttpStatus.Series.REDIRECTION);
    }

    /**
     * Whether this status code is in the HTTP series
     * {@link org.springframework.http.HttpStatus.Series#CLIENT_ERROR}.
     * <p>This is a shortcut for checking the value of {@link #series()}.
     * @since 4.0
     * @see #series()
     */
    public boolean is4xxClientError() {
        return (series() == HttpStatus.Series.CLIENT_ERROR);
    }

    /**
     * Whether this status code is in the HTTP series
     * {@link org.springframework.http.HttpStatus.Series#SERVER_ERROR}.
     * <p>This is a shortcut for checking the value of {@link #series()}.
     * @since 4.0
     * @see #series()
     */
    public boolean is5xxServerError() {
        return (series() == HttpStatus.Series.SERVER_ERROR);
    }

    /**
     * Whether this status code is in the HTTP series
     * {@link org.springframework.http.HttpStatus.Series#CLIENT_ERROR} or
     * {@link org.springframework.http.HttpStatus.Series#SERVER_ERROR}.
     * <p>This is a shortcut for checking the value of {@link #series()}.
     * @since 5.0
     * @see #is4xxClientError()
     * @see #is5xxServerError()
     */
    public boolean isError() {
        return (is4xxClientError() || is5xxServerError());
    }

    /**
     * Return a string representation of this status code.
     */
    @Override
    public String toString() {
        return this.value + " " + name();
    }


    /**
     * Return the {@code HttpStatus} enum constant with the specified numeric value.
     * @param statusCode the numeric value of the enum to be returned
     * @return the enum constant with the specified numeric value
     * @throws IllegalArgumentException if this enum has no constant for the specified numeric value
     */
    public static CustomHttpStatus valueOf(int statusCode) {
        CustomHttpStatus status = resolve(statusCode);
        if (status == null) {
            throw new IllegalArgumentException("No matching constant for [" + statusCode + "]");
        }
        return status;
    }

    /**
     * Resolve the given status code to an {@code HttpStatus}, if possible.
     * @param statusCode the HTTP status code (potentially non-standard)
     * @return the corresponding {@code HttpStatus}, or {@code null} if not found
     * @since 5.0
     */
    @Nullable
    public static CustomHttpStatus resolve(int statusCode) {
        // Use cached VALUES instead of values() to prevent array allocation.
        for (CustomHttpStatus status : VALUES) {
            if (status.value == statusCode) {
                return status;
            }
        }
        return null;
    }

}
