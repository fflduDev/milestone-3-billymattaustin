package tree;

public class testOrgChart {

	public static void main(String[] args) {

		

		// fill the org chart
		OrgChart company = new OrgChartImpl();
		
		Employee e1 = new Employee("Jack", 1001, "CEO");
		Employee e2 = new Employee("Katie", 1002, "Project Manager");
		Employee e3 = new Employee("Ben", 1021, "Designer");
		Employee e4 = new Employee("Rosy", 1001, "Lawyer");
		Employee e5 = new Employee("Tania", 1090, "Project Manager");
		Employee e6 = new Employee("Magnus", 1131, "PM Secretary");
		Employee e7 = new Employee("Anish", 1290, "Market Researcher");
		Employee e8 = new Employee("Jack B", 1324, "Coder");
		Employee e9 = new Employee("Layla", 1253, "Intern");
		Employee e10 = new Employee("Jade", 1923, "CEO Secretary");
		
		company.addRoot(e1);
		company.addDirectReport(e1, e2);
		company.addDirectReport(e1, e5);
		company.addDirectReport(e1, e10);
		company.addDirectReport(e2, e3);
		company.addDirectReport(e2, e4);
		company.addDirectReport(e5, e6);
		company.addDirectReport(e5, e7);
		company.addDirectReport(e7, e8);
		company.addDirectReport(e7, e9);
		
		// show it depth first
		System.out.println("Depth First: ");
		company.showOrgChartDepthFirst();
		System.out.println();
		
		// show breadth first
		System.out.println("Breadth First: ");
		company.showOrgChartBreadthFirst();
		System.out.println();
		
		// and remove some people
		company.removeEmployee(e4);
		company.removeEmployee(e5);
		
		// show it depth first
		System.out.println("Testing depth first after remove");
		company.showOrgChartDepthFirst();
		System.out.println();
		
		// show breadth first
		System.out.println("Testing breadth first after remove");
		company.showOrgChartBreadthFirst();
		

	}

}
