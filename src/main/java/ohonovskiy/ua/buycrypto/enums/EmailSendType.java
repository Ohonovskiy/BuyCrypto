package ohonovskiy.ua.buycrypto.enums;

import lombok.Getter;

@Getter
public enum EmailSendType {
    SUPPORT(Scope.BOTH),
    NEWS(Scope.USERS_ONLY);

    private final Scope scope;

    EmailSendType(Scope scope) {
        this.scope = scope;
    }

    public enum Scope {
        USERS_ONLY,
        SUPPORT_ONLY,
        BOTH
    }
}
