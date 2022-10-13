package domains;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Task implements Entity {

    private Long id;
    private String description;
    private Integer goldPriseAmount;

}
