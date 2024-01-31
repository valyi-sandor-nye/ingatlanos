
import java.util.TreeSet;
import java.io.*;
import java.util.StringTokenizer;

/**
 *
 * @author valyis
 */

interface IngatlanInterfész {
    
    void akcio(int p);
    int teljesar();
    double atlagos();
    // a toString()-et nem veszem be az interfészbe. 
    //Az benne van az Object osztályban, úgyis örökli, úgyis felül kell írni.
}
    /** Az ingatlan lehetséges típusai, felsorolás */
enum IngatlanTipus {CSALÁDIHÁZ, TÁRSASHÁZ, HÁZRÉSZ}

/** Ez az osztály egy ingatlan jellemzőit tárolja. */
class Ingatlan implements IngatlanInterfész, Comparable<Ingatlan> {
    String telepules;
    double ar; 
    int terulet;
    double szobaszam;
    IngatlanTipus tipus;

/** Ez a konstruktor minden jellemzőt paraméterből állít be. */
    public Ingatlan (String s, double a, int ter, double sz, IngatlanTipus t) {
    telepules = new String(s);
    ar=a;
    terulet=ter;
    szobaszam=sz;
    tipus=t;
}

    /* NIncs default konstruktor, ehelyett megatuk, hogy legyen paramétermentes konstruktor.
    Minden 0-ra, 0.0-ra, null-ra áll be.
    */
public Ingatlan() {}
    
/** ez a metódus a paraméterben egész számként megadott 
 * százalékkal csökkenti az ingatlan egy négyzetméterre jutó árát
 */    
    @Override
    public void akcio(int p) {
    ar -= p;        
    }

    /** ez a metódus egész számmal visszatér az ingatlan teljes árával, 
 * amit az ar és nm mezők segítségével számol ki. 
 * Az árat befolyásolja az, hogy melyik településen van az ingatlan, 
 * ezért ha az ingatlan Budapesten van, akkor 30%-al, h
 * a Debrecenben, akkor 20%-al, ha Nyíregyházán, akkor 15%-al nagyobb 
 * árat kell visszaadni a kiszámolt értéknél
 * 
 */
    @Override
    public int teljesar() {
        int alapar = (int)(ar * terulet);
        if (telepules.trim().equalsIgnoreCase("Budapest")) {return (int) (alapar*1.3);}
        else if (telepules.trim().equalsIgnoreCase("Debrecen")) {return (int) (alapar*1.2);}
        else if (telepules.trim().equalsIgnoreCase("Nyíregy")) {return (int) (alapar*1.15);}
        else return alapar;
    }

    /**atlagos – kiszámolja és visszaadja valós számként, 
     * hogy hány négyzetméter jut átlagosan egy szobára az adott ingatlan esetében.
     */
    @Override
    public double atlagos() {
        return terulet/szobaszam;
    }
    
    /**az ingatlan adatainak kiíratása 
     * a teljes árral és a szobákra eső átlagos négyzetméterszámmal egyetemben
     */
    @Override
    public String toString() {
    return  telepules+" "+terulet+" nm-es "+szobaszam+" szobás "+tipus+" eladó "+ar
            +" négyzetméter-áron "+atlagos()+" átlagos szobamérettel "+teljesar()+" ft teljes áron.";
    
    }

    @Override
    public int compareTo(Ingatlan t) {
        if (ar<t.ar) return -1;
        else if (ar>t.ar) return 1;
        else if (terulet<t.terulet) return -1;
        else if (terulet>t.terulet) return 1;
        else if (szobaszam<t.szobaszam) return -1;
        else if (szobaszam>t.szobaszam) return 1;
        else return 0;
    }
}
/**a Panel osztály örököl az Ingatlan osztályból. Az osztálynak két új mezője van:
* emelet – az értéke megadja, hogy az ingatlan hányadik emeleten található. (egész)
* szigetelt – igaz az értéke, hogy ha a panellakás külső szigeteléssel rendelkezik. (logikai)
*   @author valyis
 */

class Panel extends Ingatlan implements PanelInterfész{
    int emelet;
    boolean szigetelt;

Panel(String s, double a, int ter, double sz, IngatlanTipus t,int e, boolean szig){
    super(s,a,ter,sz,t);
    emelet=e;
    szigetelt=szig;
}
    
/**Ha a panellakás a 0 – 2 emeleten található, akkor +5%-al, 
 * ha a 10 emeleten, akkor -5%-al, ha szigetelt, 
 * akkor további +5%-al módosítsuk a kiszámolt teljes ár értékét.
 */    
    @Override
    public int teljesar() {
        int alapar = (int)(ar * terulet);
        int teljesar = alapar;
        if (telepules.trim().equalsIgnoreCase("Budapest")) {teljesar*=1.3;}
        else if (telepules.trim().equalsIgnoreCase("Debrecen")) {teljesar*=1.2;}
        else if (telepules.trim().equalsIgnoreCase("Nyíregy")) {teljesar*=1.15;}
        if (emelet>=0 && emelet <=2) teljesar *=1.05;
        else if (emelet==10) teljesar *=0.95;
        if (szigetelt) teljesar *=1.05;
        return teljesar;
    }

    /**ugyanannyi – igazat ad vissza, ha az Ingatlan osztálybeli bemenő paraméterének 
 ugyanakkora a teljesar metódus által visszaadott értéke
     */
    @Override
    public boolean ugyanannyi(Ingatlan ing) {
    return teljesar()==ing.teljesar();
    }
    /**visszaadja egész számként, hogy átlagosan mennyibe kerül 
     * az adott ingatlanban egy átlagos szoba. 
     * (Nem kell figyelembe venni sem a települési, 
     * sem az emeleti vagy szigetelési módosítókat.)
     * */
    @Override
    public int szobaar() {
        return (int) (ar*terulet / szobaszam);
    }
    
    @Override
    public String toString() {    return  telepules+" "+terulet+" nm-es "+szobaszam+" szobás "
            +emelet+" emeleti "+ "panel-"
            +tipus+" eladó "+ar
            +" négyzetméter-áron "+atlagos()+" átlagos szobamérettel "+teljesar()
            +" ft teljes áron.";
    }

}

interface PanelInterfész extends IngatlanInterfész{
    boolean ugyanannyi (Ingatlan ing);
    int szobaar();
}

/**Ingatlanos osztály. 
 * Az osztálynak van egy statikus ingatlanok nevű TreeSet típusú kollekciója, 
 amelyben az Ingatlan osztályba tartozó objektumokat tudunk nyilvántartani, 
 illetve egy szintén statikus panellakas mezője, 
 amely az ingatlanok között szereplő panellakások számát tartalmazza.
 */
class Ingatlanos {
    static TreeSet<Ingatlan> kinalat = new TreeSet<Ingatlan>();
    static int panelLakasokSzama;
    Ingatlanos() {
        FileReader inputStream = null;
        int c = 0;
        try {
            inputStream = new FileReader("ingatlanok.txt");
            while (c!=-1) {
             StringBuilder sorBe = new StringBuilder();
            while ((c=inputStream.read())!='\n'&& c!=-1 )
                {sorBe.append((char)c);}
            String sor = new String(sorBe);
            StringTokenizer st = new StringTokenizer(sor,"#");
            String elsoDarab = st.nextToken();
            String telepulesJelolt=st.nextToken();
            double arJelolt;
            arJelolt = (double)(Integer.parseInt(st.nextToken()));
            int teruletJelolt;
            teruletJelolt = Integer.parseInt(st.nextToken());
            double szobaszamJelolt;
            szobaszamJelolt = Integer.parseInt (st.nextToken());
            String tipusSzoveg = st.nextToken();
            IngatlanTipus tipusJelolt = null;
            for (IngatlanTipus it : IngatlanTipus.values()) {
              if (it.toString().trim().equalsIgnoreCase(tipusSzoveg)) {tipusJelolt = it;}
            }
            if (tipusJelolt == null) throw new IOException();
            if (elsoDarab.trim().equalsIgnoreCase("INGATLAN")) {
                kinalat.add(new Ingatlan(telepulesJelolt,arJelolt,teruletJelolt,
                        szobaszamJelolt,tipusJelolt));
            }
            else if (elsoDarab.trim().equalsIgnoreCase("PANEL")) {
            int emeletJelolt = Integer.parseInt(st.nextToken());
            String szigeteltJeloltMintSzoveg = st.nextToken();
            boolean szigeteltJelolt;
            if (szigeteltJeloltMintSzoveg.equalsIgnoreCase("igen")) szigeteltJelolt = true;
            else if (szigeteltJeloltMintSzoveg.equalsIgnoreCase("nem")) szigeteltJelolt = false;
            else throw new IOException();
            kinalat.add(new Panel(telepulesJelolt,arJelolt,teruletJelolt,
                        szobaszamJelolt,tipusJelolt,emeletJelolt,szigeteltJelolt));
            }
            else throw new IOException();
            }
        }
        catch (FileNotFoundException fnfe) {System.err.println("Nincs meg az inputfájl");}
        catch(IOException ioe) {System.err.println("Input fájl olvasási hiba");}
        finally{
            if (inputStream != null) 
                try {
                    inputStream.close();
                }
                catch(IOException ioe){System.err.println("Input fájl lezárási hiba");}
    }
    }
}

/** Ez a programocska a ProhIIZH2016 megoldását adja. Az ósztály adattagjaiul 
 * felveszi a kiszámítandó értékeket, s a konstruktorában egy közös ciklusban kiszámítja azokat. 
 * Ez azért hasznos, mert így egyszer fut csak végig a tárolt ingatlanok kollekcióján, 
 * s egyszerre az összes krdésre kiszámítja a választ. Ha ezeket a ciklusokat az
 * egyes metódusokba helyezzük, akkor ahány metódushívás történik, annyiszor
 * fut végig a teljes ingatlan-kolllekción. A metódusokba való kihelyezés jobb tesztelhetőség miatt 
 * hasznos.
 * @author valyis
 */
public class IngatlanAlkalmazas {
        Ingatlanos ingatlanos = new Ingatlanos();
        TreeSet<Ingatlan> ingatlanLista = ingatlanos.kinalat;
        double nm_osszeg = 0; 
        int ar_osszeg = 0;
        double eddigi_minimum_ar = Double.MAX_VALUE;
        Ingatlan eddigi_minimum_aras_ingatlan = null;
        double eddigi_legdragabb_budapesti_lakas_ara = 0;
        Ingatlan eddigi_legdragabb_budapesti_lakas = null;
        int osszesitett_ar = 0;
        int teljes_arak_osszege = 0;
        int ingatlanok_darabszama = ingatlanLista.size();  
    public IngatlanAlkalmazas() {
        for (Ingatlan ing: ingatlanLista) {
            nm_osszeg += ing.terulet;
            ar_osszeg += ing.ar;
            if (ing.ar < eddigi_minimum_ar) {
            eddigi_minimum_ar = ing.ar;
            eddigi_minimum_aras_ingatlan = ing;
            }
            if (ing.telepules.equalsIgnoreCase("Budapest") && 
                    ing.ar > eddigi_legdragabb_budapesti_lakas_ara) {
               eddigi_legdragabb_budapesti_lakas_ara = ing.ar; 
               eddigi_legdragabb_budapesti_lakas = ing;
            }
            osszesitett_ar+=ing.ar;
            teljes_arak_osszege+=ing.teljesar();
        }
   
    }
    double nm_osszeg() {return nm_osszeg;}
    int ar_osszeg() {return ar_osszeg;}
    double minimum_ar() {return eddigi_minimum_ar;}
    Ingatlan legdragabb_budapesti_lakas(){return eddigi_legdragabb_budapesti_lakas;}
    int osszesitett_ar() {return osszesitett_ar;}
    int teljes_arak_osszege() {return teljes_arak_osszege;}
    
    
    public static void main(String[] a) {
        IngatlanAlkalmazas ia = new IngatlanAlkalmazas();
        System.out.println("Az ingatlanok átlagos nm-ára:"+(int)(ia.ar_osszeg/ia.nm_osszeg()));
        System.out.println("A legolcsóbb ingatlan ára: "+ia.minimum_ar());
        System.out.println("A legdrágább budpesti lakás egy szobára eső átlagos nm-e:"+
                ia.legdragabb_budapesti_lakas().atlagos());
        System.out.println("Az összesített ár: "+ia.osszesitett_ar());
        System.out.println("Azon társasházi ingatlanok felsorolása,+"
                + "melyek teljes ára nem haladja meg az ingatlanok telejes árának átlagát:");
        for (Ingatlan ing: ia.ingatlanLista) {
            if (ing.teljesar() < 
                    ia.teljes_arak_osszege()/ia.ingatlanok_darabszama) {
             System.out.println(ing);
            }
        }
        
    }

}