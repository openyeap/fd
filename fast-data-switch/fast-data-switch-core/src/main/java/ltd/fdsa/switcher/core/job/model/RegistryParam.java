package ltd.fdsa.switcher.core.job.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RegistryParam implements Serializable {
    private static final long serialVersionUID = 42L;
    private String registryGroup;
    private String registryKey;
    private String registryValue;
}
