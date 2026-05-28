package mx.uv.sigomei.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

import mx.uv.sigomei.dto.CredencialesDTO;
import mx.uv.sigomei.dto.SesionDTO;
import mx.uv.sigomei.exception.AutenticacionException;
import mx.uv.sigomei.exception.CuentaBloqueadaException;

public interface IAuthService extends Remote {
    SesionDTO login(CredencialesDTO credenciales)
            throws RemoteException, AutenticacionException, CuentaBloqueadaException;

    void logout(String token) throws RemoteException;
}
