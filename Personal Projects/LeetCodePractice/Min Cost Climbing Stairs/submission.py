class Solution:
    # This solution loops through every stair and finds the minimum cost to go to that stair
    def minCostClimbingStairs(self, cost: List[int]) -> int:
        oneAway = 0
        twoAway = 0

        for i in range(len(cost)):
            # Find the minimum cost to go to every stair
            curStair = cost[i] + min(oneAway, twoAway)

            # Go to thee next step
            twoAway = oneAway
            oneAway = curStair

        return min(oneAway, twoAway)
