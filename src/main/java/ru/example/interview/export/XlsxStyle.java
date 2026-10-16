package ru.example.interview.export;

/** Неизменяемое описание стиля ячейки. Дубликаты схлопываются в XlsxBook. Цвета — RRGGBB. */
public record XlsxStyle(boolean bold, boolean italic, double size, String color, String fill, boolean border,
                        String halign, String valign, boolean wrap, String numFmt) {

    public static final XlsxStyle BASE = new XlsxStyle(false, false, 10, "000000", null, false, null, "top", false, null);

    public XlsxStyle b() {
        return new XlsxStyle(true, italic, size, color, fill, border, halign, valign, wrap, numFmt);
    }

    public XlsxStyle i() {
        return new XlsxStyle(bold, true, size, color, fill, border, halign, valign, wrap, numFmt);
    }

    public XlsxStyle size(double v) {
        return new XlsxStyle(bold, italic, v, color, fill, border, halign, valign, wrap, numFmt);
    }

    public XlsxStyle color(String rgb) {
        return new XlsxStyle(bold, italic, size, rgb, fill, border, halign, valign, wrap, numFmt);
    }

    public XlsxStyle fill(String rgb) {
        return new XlsxStyle(bold, italic, size, color, rgb, border, halign, valign, wrap, numFmt);
    }

    public XlsxStyle bd() {
        return new XlsxStyle(bold, italic, size, color, fill, true, halign, valign, wrap, numFmt);
    }

    public XlsxStyle h(String align) {
        return new XlsxStyle(bold, italic, size, color, fill, border, align, valign, wrap, numFmt);
    }

    public XlsxStyle v(String align) {
        return new XlsxStyle(bold, italic, size, color, fill, border, halign, align, wrap, numFmt);
    }

    public XlsxStyle w() {
        return new XlsxStyle(bold, italic, size, color, fill, border, halign, valign, true, numFmt);
    }

    public XlsxStyle fmt(String code) {
        return new XlsxStyle(bold, italic, size, color, fill, border, halign, valign, wrap, code);
    }
}
