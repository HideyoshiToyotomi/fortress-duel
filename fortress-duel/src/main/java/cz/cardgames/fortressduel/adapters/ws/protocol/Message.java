package cz.cardgames.fortressduel.adapters.ws.protocol;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,  // typ se bere podle jména
        property = "type"            // jméno pole v JSONu, např. {"type":"login", ...}
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Register.class,   name = "register"),
        @JsonSubTypes.Type(value = Login.class,      name = "login"),
        @JsonSubTypes.Type(value = Registered.class, name = "registered"),
        @JsonSubTypes.Type(value = LoggedIn.class,   name = "loggedIn"),
        @JsonSubTypes.Type(value = ErrorMessage.class,   name = "error")
})
public sealed interface Message
        permits Register, Login, Registered, LoggedIn, ErrorMessage {
}
