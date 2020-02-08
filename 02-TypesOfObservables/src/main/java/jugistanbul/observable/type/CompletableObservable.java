package jugistanbul.observable.type;

import io.reactivex.Completable;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author hakdogan (hakdogan@kodcu.com)
 * Created on 8.02.2020
 **/

public class CompletableObservable {

    private static final String FILEPATH = "02-TypesOfObservables/src/main/resources/";

    public static void main(String[] args){
        getCompletableObserver().subscribe(() -> System.out.println("Process completed..."), System.out::println);
    }

    static Completable getCompletableObserver(){
        return Completable.create(completable -> {
            try {
                writeFile("myLog.log", "some results");
                completable.onComplete();
            } catch (IOException io){
                completable.onError(io);
            }
        });
    }
    static void writeFile(final String file, final String text) throws IOException {
        final Path path = Paths.get(FILEPATH + file);
        try(BufferedWriter writer = Files.newBufferedWriter(path, Charset.forName("UTF-8"))){
            writer.write(text);
        }catch(IOException ex){
            throw new IOException();
        }
    }
}
