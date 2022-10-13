package domains;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User implements Entity {

    private Long id;
    private String name;
    private Integer gold;
    private Long clanId;

    public synchronized User copy() {
        return User.builder()
                .id(id)
                .name(name)
                .gold(gold)
                .clanId(clanId)
                .build();
    }

}
