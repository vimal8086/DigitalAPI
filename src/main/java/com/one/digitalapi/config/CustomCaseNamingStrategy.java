package com.one.digitalapi.config;

import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

public class CustomCaseNamingStrategy extends CamelCaseToUnderscoresNamingStrategy {

    @Override
    public Identifier toPhysicalSchemaName(Identifier logicalName, JdbcEnvironment context) {
        if (logicalName == null) {
            return null;
        }

        final String newName = logicalName.getText();
        return Identifier.toIdentifier(newName.toUpperCase());
    }

    @Override
    public Identifier toPhysicalColumnName(Identifier logicalName, JdbcEnvironment context) {

        var identifier = super.toPhysicalColumnName(logicalName, context);
        if (identifier == null) {
            return null;
        }

        final String newName = identifier.getText();
        return Identifier.toIdentifier(newName.toUpperCase());
    }

}
