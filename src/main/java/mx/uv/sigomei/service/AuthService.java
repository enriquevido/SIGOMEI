package mx.uv.sigomei.service;

import mx.uv.sigomei.dto.CredencialesDTO;
import mx.uv.sigomei.dto.SesionDTO;

public interface AuthService {
    SesionDTO login(CredencialesDTO credenciales);

    void logout(String token);
}
