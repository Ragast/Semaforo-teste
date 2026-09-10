# Semáforo Timer — versão de teste

App Android muito simples para um semáforo alternado de ciclo fixo.

## Funcionamento
- Ecrã inteiro verde/vermelho.
- Contagem decrescente até à próxima mudança.
- Botão `sync` pequeno no canto inferior esquerdo.
- Premir `sync` exatamente quando o semáforo fica verde.
- Não usa GPS, internet, câmara ou localização.

## Alterar os tempos
Em `MainActivity.kt`, altera:
- `greenSeconds`
- `redSeconds`

Depois compila no Android Studio.

A versão de teste incluída neste pacote assume inicialmente 60 s verde + 60 s vermelho.
