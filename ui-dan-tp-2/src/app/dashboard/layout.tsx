import SideNav from "@/components/SideNav"

export default function layoutDashboard( 
    { children } : { children: React.ReactNode } 
) 
{
	return (
        <div className="flex h-screen flex-col md:flex-row md:overflow-hidden">
          <div className="w-full flex-none md:w-64">
            <SideNav />
          </div>
          <div className="grow md:overflow-y-auto md:p-4">{children}</div>
        </div>
		)
}