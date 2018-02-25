package org.hoby.nye.thank_you_matcher.utility;

import java.security.InvalidParameterException;

public class ZipCode implements Comparable<ZipCode> {
    private Integer zip;
    private Integer plus4;

    public ZipCode(Integer zip) {
        this(zip, -1 );
    }

    public ZipCode(Integer zip, Integer plus4) {
        this.zip   = zip;
        this.plus4 = plus4;
    }

    public ZipCode(String stringCellValue) {
        String[] splitCells = stringCellValue.split("-");
        if (splitCells.length == 2)
        {
            this.zip = Integer.parseInt(splitCells[0]);
            this.plus4 = Integer.parseInt(splitCells[1]);
        }
        else if (splitCells.length == 1)
        {
            this.zip = Integer.parseInt(splitCells[0]);
            this.plus4 = -1;
        }
        else
        {
            throw new InvalidParameterException("zip code cell contained no data");
        }

    }

    /*
     * (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(ZipCode o) {
        if( this.equals(o)) {
            return 0;
        }

        if( zip < o.zip || ( zip.equals( o.zip ) && plus4 < o.plus4 ) ) {
            return -1;
        } else if( zip.equals( o.zip ) && plus4.equals( o.plus4 ) ) {
            return 0;
        } else if( zip > o.zip || ( zip.equals( o.zip ) && plus4 > o.plus4 ) ) {
            return 1;
        }

        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ZipCode zipCode = (ZipCode) o;

        return zip.equals(zipCode.zip) && plus4.equals(zipCode.plus4);
    }

    @Override
    public int hashCode() {
        int result = zip.hashCode();
        result = 31 * result + plus4.hashCode();
        return result;
    }
}
