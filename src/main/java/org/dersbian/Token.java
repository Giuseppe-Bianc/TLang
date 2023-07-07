package org.dersbian;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

@Data
@Setter
@AllArgsConstructor
public class Token {
    private TokenType type;
    private String value;
    private int line;
    private int column;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Token{");
        sb.append("type=").append(type);
        if(!value.isBlank()) {
            sb.append(", value='").append(value).append('\'');
        }
        sb.append(", line=").append(line);
        sb.append(", column=").append(column);
        sb.append('}');
        return sb.toString();
    }
}