package jugistanbul.observable.operator;

import io.reactivex.Observable;

/**
 * @author hakdogan (hakdogan@kodcu.com)
 * Created on 8.02.2020
 **/

public class Operators {

    public static void main(String[] args){


        mapOperator().subscribe(System.out::println);
        System.out.println();

        repeatOperator(3).subscribe(System.out::println);
        System.out.println();

        distinctOperator().subscribe(System.out::println);
        System.out.println();

        getEvent()
                .map(Speaker::speakerObservable)
                .flatMap(s -> s)
                .subscribe(s-> System.out.println("Speaker: " + s.getFirstName() + " Talk Title: " + s.getTalkTilte()));

        System.out.println();

        Observable.zip(firstNamesObserbable(), lastNamesObserbable(),
                (f, b) -> new Speaker(f, b))
                .subscribe(speaker -> System.out.println(speaker.getFirstName() + " " + speaker.getLastName()));
    }

    static Observable mapOperator(){
        return Observable
                .range(0, 'Z' - 'A' + 1)
                .map(c -> (char) ('A' + c));
    }

    static Observable repeatOperator(final int count){
        return Observable.just("Hello!").repeat(count);
    }

    static Observable distinctOperator(){
        return Observable.just("A", "B", "A", "C", "D", "B", "E").distinct();
    }

    static Observable<Event> getEvent(){
        return Observable.just(new Event("Java Day İstanbul"));
    }

    static Observable<String> firstNamesObserbable(){
        return Observable.just("Lemi Orhan",
                "Kevin",
                "Gunnar",
                "Alberto",
                "Mete",
                "Sebastian",
                "Mohamed",
                "Oleh",
                "Nicolas",
                "Claus");
    }

    static Observable<String> lastNamesObserbable(){
        return Observable.just("Ergin",
                "Wittek",
                "Morling",
                "Salazar",
                "Atamel",
                "Daschner",
                "Taman",
                "Dokuka",
                "Frankel",
                "Ibsen");
    }
}


