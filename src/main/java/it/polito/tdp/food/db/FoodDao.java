package it.polito.tdp.food.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.food.model.Adiacenza;
import it.polito.tdp.food.model.Condiment;
import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.Portion;

public class FoodDao
{
	public List<Food> listAllFoods()
	{
		String sql = "SELECT * FROM food";
		try
		{
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			List<Food> list = new ArrayList<>();

			ResultSet res = st.executeQuery();

			while (res.next())
			{
				try
				{
					list.add(new Food(res.getInt("food_code"), res.getString("display_name"), 0.0));
				}
				catch (Throwable t)
				{
					t.printStackTrace();
				}
			}

			conn.close();
			return list;

		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}

	}

	public List<Condiment> listAllCondiments()
	{
		String sql = "SELECT * FROM condiment";
		try
		{
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			List<Condiment> list = new ArrayList<>();

			ResultSet res = st.executeQuery();

			while (res.next())
			{
				try
				{
					list.add(new Condiment(res.getInt("condiment_code"), res.getString("display_name"),
							res.getDouble("condiment_calories"), res.getDouble("condiment_saturated_fats")));
				}
				catch (Throwable t)
				{
					t.printStackTrace();
				}
			}

			conn.close();
			return list;

		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public List<Portion> listAllPortions()
	{
		String sql = "SELECT * FROM portion";
		try
		{
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			List<Portion> list = new ArrayList<>();

			ResultSet res = st.executeQuery();

			while (res.next())
			{
				try
				{
					list.add(new Portion(res.getInt("portion_id"), res.getDouble("portion_amount"),
							res.getString("portion_display_name"), res.getDouble("calories"),
							res.getDouble("saturated_fats"), res.getInt("food_code")));
				}
				catch (Throwable t)
				{
					t.printStackTrace();
				}
			}

			conn.close();
			return list;

		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}

	}

	public void getArchi(Map<Integer, Food> foods, int amount)
	{
		String sql = "SELECT * "
					+ "FROM  food_pyramid_mod.food AS f, food_pyramid_mod.portion AS p "
					+ "WHERE f.food_code = p.food_code " 
					+ "AND p.portion_amount >= ?";
		try
		{
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, amount);
			ResultSet res = st.executeQuery();

			while (res.next())
			{
				try
				{
					if (!foods.containsKey(res.getInt("food_code")))
					{
						Food f = new Food(res.getInt("food_code"), res.getString("display_name"), res.getDouble("solid_fats"));
						foods.put(f.getFood_code(), f);
					}
				}
				catch (Throwable t)
				{
					t.printStackTrace();
				}
			}

			conn.close();

		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public List<Adiacenza> getAdiacenza(Map<Integer, Food> foods, int amount)
	{
		String sql = "SELECT t1.food_code AS id1, t2.food_code AS id2, (t1.avg - t2.avg) AS peso "
				+ "FROM  (SELECT f.food_code, AVG(p.saturated_fats) AS avg "
				+ "		FROM food_pyramid_mod.food AS f, food_pyramid_mod.portion AS p "
				+ "		WHERE f.food_code = p.food_code "
				+ "		AND p.portion_amount >= ? "
				+ "		GROUP BY f.food_code) AS t1, "
				+ "		(SELECT f.food_code, AVG(p.saturated_fats) AS avg "
				+ "		FROM food_pyramid_mod.food AS f, food_pyramid_mod.portion AS p "
				+ "		WHERE f.food_code = p.food_code "
				+ "		AND p.portion_amount >= ? "
				+ "		GROUP BY f.food_code) AS t2 "
				+ "WHERE t1.food_code < t2.food_code "
				+ "HAVING peso > 0";

		List<Adiacenza> list = new ArrayList<>();
		try
		{
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, amount);
			st.setInt(2, amount);
			ResultSet res = st.executeQuery();

			while (res.next())
			{
				try
				{
					Food f1 = foods.get(res.getInt("id1"));
					Food f2 = foods.get(res.getInt("id2"));

					if (f1 != null && f2 != null)
					{
						Adiacenza a = new Adiacenza(f1, f2, res.getDouble("peso"));
//						System.out.println("****" + a);
						list.add(a);
					}
				}
				catch (Throwable t)
				{
					t.printStackTrace();
				}
			}
			conn.close();
			return list;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
