package example.com.chamedurefact.apiPayload.exception;

import example.com.chamedurefact.apiPayload.code.BaseErrorCode;
import example.com.chamedurefact.apiPayload.code.ErrorReasonDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {
    private BaseErrorCode code;

    public ErrorReasonDto getErrorReason() {
        return this.code.getReason();
    }

    public ErrorReasonDto getErrorReasonHttpStatus(){
        return this.code.getReasonHttpStatus();
    }
}
