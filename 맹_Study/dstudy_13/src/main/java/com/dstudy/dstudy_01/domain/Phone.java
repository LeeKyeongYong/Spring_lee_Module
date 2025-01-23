package com.dstudy.dstudy_01.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public record Phone(
        @Column(name = "home") String home,
        @Column(name = "office") String office,
        @Column(name = "fax") String fax
) {
    // Compact constructor for validation
    public Phone {
        home = home != null ? home : "";
        office = office != null ? office : "";
        fax = fax != null ? fax : "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Phone phone = (Phone) o;
        return Objects.equals(home, phone.home) &&
                Objects.equals(office, phone.office) &&
                Objects.equals(fax, phone.fax);
    }

    @Override
    public int hashCode() {
        return Objects.hash(home, office, fax);
    }
}
