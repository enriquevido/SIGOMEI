package mx.uv.sigomei.remote.impl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;

import mx.uv.sigomei.dto.CredencialesDTO;
import mx.uv.sigomei.dto.SesionDTO;
import mx.uv.sigomei.remote.IAuthService;
import mx.uv.sigomei.service.AuthService;

public class AuthRemoteService extends UnicastRemoteObject implements IAuthService {
    private static final long serialVersionUID = 1L;

    private final AuthService authService;

    public AuthRemoteService(AuthService authService, boolean sslEnabled) throws RemoteException {
        this(authService, 0, sslEnabled);
    }

    public AuthRemoteService(AuthService authService, int port, boolean sslEnabled) throws RemoteException {
        super(port,
                sslEnabled ? new SslRMIClientSocketFactory() : null,
                sslEnabled ? new SslRMIServerSocketFactory() : null);
        this.authService = authService;
    }

    @Override
    public SesionDTO login(CredencialesDTO credenciales) throws RemoteException {
        return authService.login(credenciales);
    }

    @Override
    public void logout(String token) throws RemoteException {
        authService.logout(token);
    }
}
