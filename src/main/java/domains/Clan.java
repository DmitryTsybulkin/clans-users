package domains;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Clan implements Entity {

    private Long id;
    private String name;
    private Integer gold;

    public Clan copy() {
        return Clan.builder()
                .id(id)
                .name(name)
                .gold(gold)
                .build();
    }

}
