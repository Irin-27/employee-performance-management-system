// src/components/dashboard/Dashboard.jsx
import React, { useState, useEffect } from 'react';
import { useAuth } from '../../context/AuthContext';
import axios from 'axios';

const Dashboard = () => {
  const { user, logout, hasRole } = useAuth();
  const [stats, setStats] = useState({
    totalEmployees: 0,
    activeReviews: 0,
    completedGoals: 0,
    pendingTasks: 0
  });
  const [recentActivities, setRecentActivities] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchDashboardData();
  }, []);

  const fetchDashboardData = async () => {
    try {
      // Mock data for now - replace with actual API calls later
      setStats({
        totalEmployees: 150,
        activeReviews: 12,
        completedGoals: 85,
        pendingTasks: 5
      });
      
      setRecentActivities([
        {
          id: 1,
          type: 'review',
          message: 'Performance review submitted for Q4 2024',
          time: '2 hours ago',
          icon: '📊'
        },
        {
          id: 2,
          type: 'goal',
          message: 'Goal "Improve Customer Satisfaction" updated',
          time: '1 day ago',
          icon: '🎯'
        },
        {
          id: 3,
          type: 'feedback',
          message: 'Received feedback from manager',
          time: '2 days ago',
          icon: '💬'
        }
      ]);
      
      setLoading(false);
    } catch (error) {
      console.error('Error fetching dashboard data:', error);
      setLoading(false);
    }
  };

  const StatCard = ({ title, value, icon, color, description }) => (
    <div className="bg-white overflow-hidden shadow rounded-lg">
      <div className="p-5">
        <div className="flex items-center">
          <div className="flex-shrink-0">
            <div className={`w-8 h-8 ${color} rounded-md flex items-center justify-center`}>
              {icon}
            </div>
          </div>
          <div className="ml-5 w-0 flex-1">
            <dl>
              <dt className="text-sm font-medium text-gray-500 truncate">
                {title}
              </dt>
              <dd className="text-lg font-medium text-gray-900">
                {value}
              </dd>
            </dl>
          </div>
        </div>
        {description && (
          <div className="mt-2">
            <p className="text-sm text-gray-600">{description}</p>
          </div>
        )}
      </div>
    </div>
  );

  const QuickActionCard = ({ title, description, icon, onClick, color }) => (
    <div 
      className="bg-white p-6 rounded-lg shadow cursor-pointer hover:shadow-md transition-shadow duration-200"
      onClick={onClick}
    >
      <div className="flex items-center">
        <div className={`w-12 h-12 ${color} rounded-lg flex items-center justify-center text-white text-xl`}>
          {icon}
        </div>
        <div className="ml-4">
          <h3 className="text-lg font-medium text-gray-900">{title}</h3>
          <p className="text-sm text-gray-500">{description}</p>
        </div>
      </div>
    </div>
  );

  if (loading) {
    return (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center">
        <div className="animate-spin rounded-full h-32 w-32 border-b-2 border-indigo-600"></div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <header className="bg-white shadow">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center py-6">
            <div>
              <h1 className="text-3xl font-bold text-gray-900">
                Welcome back, {user?.firstName}!
              </h1>
              <p className="text-sm text-gray-600 mt-1">
                {user?.jobTitle} • {user?.department}
              </p>
            </div>
            <div className="flex items-center space-x-4">
              <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-green-100 text-green-800">
                {user?.role}
              </span>
              <button
                onClick={logout}
                className="bg-red-600 hover:bg-red-700 text-white px-4 py-2 rounded-md text-sm font-medium transition duration-150 ease-in-out"
              >
                Logout
              </button>
            </div>
          </div>
        </div>
      </header>

      <main className="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
        {/* Stats Grid */}
        <div className="px-4 py-6 sm:px-0">
          <div className="grid grid-cols-1 gap-5 sm:grid-cols-2 lg:grid-cols-4">
            <StatCard
              title="Total Employees"
              value={stats.totalEmployees}
              icon="👥"
              color="bg-blue-500"
              description="Active team members"
            />
            <StatCard
              title="Active Reviews"
              value={stats.activeReviews}
              icon="📋"
              color="bg-green-500"
              description="In progress reviews"
            />
            <StatCard
              title="Completed Goals"
              value={`${stats.completedGoals}%`}
              icon="🎯"
              color="bg-purple-500"
              description="Goals achievement rate"
            />
            <StatCard
              title="Pending Tasks"
              value={stats.pendingTasks}
              icon="⏳"
              color="bg-orange-500"
              description="Requires attention"
            />
          </div>
        </div>

        {/* Main Content */}
        <div className="px-4 py-6 sm:px-0">
          <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
            {/* Quick Actions */}
            <div className="lg:col-span-2">
              <h2 className="text-lg font-medium text-gray-900 mb-4">Quick Actions</h2>
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <QuickActionCard
                  title="Performance Review"
                  description="View and submit reviews"
                  icon="📊"
                  color="bg-blue-600"
                  onClick={() => console.log('Navigate to reviews')}
                />
                <QuickActionCard
                  title="Goals Management"
                  description="Set and track goals"
                  icon="🎯"
                  color="bg-green-600"
                  onClick={() => console.log('Navigate to goals')}
                />
                {hasRole('MANAGER') && (
                  <>
                    <QuickActionCard
                      title="Team Management"
                      description="Manage your team"
                      icon="👥"
                      color="bg-purple-600"
                      onClick={() => console.log('Navigate to team')}
                    />
                    <QuickActionCard
                      title="Reports"
                      description="View team reports"
                      icon="📈"
                      color="bg-indigo-600"
                      onClick={() => console.log('Navigate to reports')}
                    />
                  </>
                )}
                {hasRole('ADMIN') && (
                  <>
                    <QuickActionCard
                      title="User Management"
                      description="Manage all users"
                      icon="⚙️"
                      color="bg-red-600"
                      onClick={() => console.log('Navigate to users')}
                    />
                    <QuickActionCard
                      title="System Settings"
                      description="Configure system"
                      icon="🔧"
                      color="bg-gray-600"
                      onClick={() => console.log('Navigate to settings')}
                    />
                  </>
                )}
              </div>
            </div>

            {/* Recent Activities */}
            <div>
              <h2 className="text-lg font-medium text-gray-900 mb-4">Recent Activities</h2>
              <div className="bg-white shadow rounded-lg">
                <ul className="divide-y divide-gray-200">
                  {recentActivities.map((activity) => (
                    <li key={activity.id} className="p-4">
                      <div className="flex space-x-3">
                        <div className="text-2xl">{activity.icon}</div>
                        <div className="flex-1 space-y-1">
                          <p className="text-sm text-gray-900">{activity.message}</p>
                          <p className="text-xs text-gray-500">{activity.time}</p>
                        </div>
                      </div>
                    </li>
                  ))}
                </ul>
                {recentActivities.length === 0 && (
                  <div className="p-4 text-center text-gray-500">
                    No recent activities
                  </div>
                )}
              </div>
            </div>
          </div>
        </div>

        {/* Performance Overview */}
        <div className="px-4 py-6 sm:px-0">
          <div className="bg-white shadow rounded-lg p-6">
            <h2 className="text-lg font-medium text-gray-900 mb-4">Performance Overview</h2>
            <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
              <div className="text-center">
                <div className="text-3xl font-bold text-blue-600">85%</div>
                <div className="text-sm text-gray-500">Goal Completion</div>
              </div>
              <div className="text-center">
                <div className="text-3xl font-bold text-green-600">4.2/5</div>
                <div className="text-sm text-gray-500">Average Rating</div>
              </div>
              <div className="text-center">
                <div className="text-3xl font-bold text-purple-600">12</div>
                <div className="text-sm text-gray-500">Reviews Completed</div>
              </div>
            </div>
          </div>
        </div>
      </main>
    </div>
  );
};

export default Dashboard;