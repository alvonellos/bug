package com.alvonellos.bug.dto;

import com.alvonellos.bug.repo.dao.KVEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Validated
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KVDTO {
    @JsonProperty("kv_id")
    private Long id;

    @NotBlank
    @JsonProperty("kv_key")
    private String key;

    @Nullable
    @JsonProperty("kv_value")
    private String value;

    public KVDTO(String key, String value) {
        this(null, key, value);
    }

    public KVDTO(KVEntity kvEntity) {
        this.id = kvEntity.getKVEntityId();
        this.key = kvEntity.getKVEntityKey();
        this.value = kvEntity.getKVEntityValue();
    }
}
