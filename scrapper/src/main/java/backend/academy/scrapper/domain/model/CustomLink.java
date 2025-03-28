package backend.academy.scrapper.domain.model;

public interface CustomLink {
    static CustomLink of(String link) {
        return new CustomLink() {
            @Override
            public String getLink() {
                return link;
            }

            @Override
            public int hashCode() {
                return linkHashCode();
            }

            @Override
            public boolean equals(Object obj) {
                if (obj == null) return false;
                if (obj instanceof CustomLink link1) {
                    return linkEquals(link1);
                }
                return false;
            }
        };
    }

    String getLink();

    default int linkHashCode() {
        return getLink().hashCode();
    }

    default boolean linkEquals(CustomLink link) {
        if (link == null) return false;
        return link.getLink().equals(getLink());
    }
}
