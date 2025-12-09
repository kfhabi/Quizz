package quizz.bt.quizz.Entity.Enum;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RoleConverter implements AttributeConverter<Role, String> {

    @Override
    public String convertToDatabaseColumn(Role attribute) {
        return attribute == null ? null : attribute.name();
    }

    @Override
    public Role convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        try {
            return Role.valueOf(dbData);
        } catch (IllegalArgumentException ex) {
            // try case-insensitive match
            try {
                return Role.valueOf(dbData.toUpperCase());
            } catch (IllegalArgumentException ex2) {
                return null;
            }
        }
    }
}
