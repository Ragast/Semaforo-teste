# Semáforo Timer — versão de teste

App Android simples para um semáforo alternado de ciclo fixo.

## Funcionamento
- Ecrã inteiro verde/vermelho.
- Contagem decrescente até à próxima mudança.
- Botão `sync` pequeno no canto inferior esquerdo.
- Premir `sync` exatamente quando o semáforo fica verde.
- A sincronização é guardada no telemóvel.
- Ao fechar e reabrir a app, o ciclo é calculado novamente usando o relógio atual.
- Não usa GPS, internet, câmara ou localização.

## Alterar os tempos
Em `MainActivity.kt`, altera:
- `greenSeconds`
- `redSeconds`

A versão de teste usa inicialmente 60 s verde + 60 s vermelho.
