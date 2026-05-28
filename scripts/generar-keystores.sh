#!/bin/bash
# Genera server.jks y client.jks con certificado autofirmado para demo.

keytool -genkeypair -alias sigomei-server \
  -keyalg RSA -keysize 2048 \
  -validity 365 \
  -keystore config/server.jks \
  -storepass sigomei123 \
  -keypass sigomei123 \
  -dname "CN=SIGOMEI Server, OU=LIS-601, O=UV, L=Coatzacoalcos, ST=Veracruz, C=MX"

keytool -exportcert -alias sigomei-server \
  -keystore config/server.jks \
  -storepass sigomei123 \
  -file /tmp/sigomei-server.cer

keytool -importcert -noprompt \
  -alias sigomei-server \
  -file /tmp/sigomei-server.cer \
  -keystore config/client.jks \
  -storepass sigomei123

echo "Keystores generados en config/"
