package util;

package abstract class Repo {
	
	public static void init(Repo repo) {Repo.instance = repo;}

	public static CarRepo cars() { return instance.carRepo(); }

	protected abstract CarRepo carRepo();

	private static Repo instance;
}