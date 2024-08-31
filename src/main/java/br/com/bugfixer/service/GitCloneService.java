package br.com.bugfixer.service;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class GitCloneService {

    public String cloneRepository(String repoUrl) throws GitAPIException, IOException {
        File tempDir = File.createTempFile("repo", "");
        if (!tempDir.delete()) {
            throw new IOException("Não foi possível deletar o arquivo temporário " + tempDir.getAbsolutePath());
        }
        if (!tempDir.mkdirs()) {
            throw new IOException("Não foi possível criar o diretório " + tempDir.getAbsolutePath());
        }

        Git.cloneRepository().setURI(repoUrl).setDirectory(tempDir).call();
        return tempDir.getAbsolutePath();
    }
}
