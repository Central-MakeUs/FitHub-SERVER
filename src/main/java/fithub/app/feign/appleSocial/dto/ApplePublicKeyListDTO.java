package fithub.app.feign.appleSocial.dto;

import fithub.app.base.Code;
import fithub.app.base.exception.handler.AppleOAuthException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ApplePublicKeyListDTO {

    private List<ApplePublicKeyDTO> keys;

    public ApplePublicKeyDTO getMatchesKey(String alg, String kid) {
        return this.keys
                .stream()
                .filter(k -> k.getAlg().equals(alg) && k.getKid().equals(kid))
                .findFirst()
                .orElseThrow(() -> new AppleOAuthException(Code.FAILED_TO_FIND_AVALIABLE_RSA));
    }
}
