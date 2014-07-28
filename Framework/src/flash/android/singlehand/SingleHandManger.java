package flash.android.singlehand;

public final class SingleHandManger
{
	public static int s_viewWidth = 204;
	public static int s_viewHeight = 363;
	private static boolean s_isSingleHand = false;

	public static void switchMode()
	{
		s_isSingleHand = !s_isSingleHand;
	}

	public static boolean isSingleHand()
	{
		return s_isSingleHand;
	}

}
