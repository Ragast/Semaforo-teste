# Semáforo Timer — GitHub

## Carregar o projeto
1. Cria um repositório chamado `semaforo-timer`.
2. Abre o repositório e escolhe **Add file → Upload files**.
3. Arrasta para a página TODO o conteúdo desta pasta, incluindo a pasta `.github`.
4. Faz **Commit changes**.

## Gerar o APK
O GitHub Actions deve arrancar automaticamente após o commit.

Para ver o resultado:
1. Abre o separador **Actions**.
2. Entra em **Build Android APK**.
3. Quando terminar com um visto verde, abre a execução.
4. No fundo da página, em **Artifacts**, descarrega `semaforo-timer-debug`.
5. Dentro do ZIP estará `app-debug.apk`.

Também podes iniciar manualmente em:
**Actions → Build Android APK → Run workflow**.

## Nota
A versão inicial usa 60 segundos de verde e 60 segundos de vermelho.
Esses valores serão ajustados quando medires o semáforo real.
